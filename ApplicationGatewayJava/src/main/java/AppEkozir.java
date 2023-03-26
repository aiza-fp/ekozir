/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.GatewayException;
import org.hyperledger.fabric.client.SubmitException;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

public final class AppEkozir {

	private static String MSP_ID;
	private static final String CHANNEL_NAME = "kanala";
	private static final String CHAINCODE_NAME = "ekozir";

	// Path to crypto materials.
	private static Path CRYPTO_PATH;
	// Path to user certificate.
	private static Path CERT_PATH;
	// Path to user private key directory.
	private static Path KEY_DIR_PATH;
	// Path to peer tls certificate.
	private static Path TLS_CERT_PATH;

	// Gateway peer end point.
	private static String PEER_ENDPOINT;
	private static String OVERRIDE_AUTH;

	private final Contract contract;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static final Scanner teclado = new Scanner(System.in);

	public static void main(final String[] args) throws Exception {

		int opcion = 0;

		System.out.println("");
		System.out.println("Identifícate:");
		System.out.println("");
		System.out.println("1. FP Zornotza");
		System.out.println("2. Recymet");
		System.out.println("3. Ormazabal");
		System.out.println("4. SALIR");
		System.out.println("");
		System.out.print("Selecciona opción: ");
		opcion = teclado.nextInt();

		switch (opcion) {
		case 1:
			MSP_ID = "org-fpzornotza-com";
			CRYPTO_PATH = Paths.get("/home/ekozir/fpzornotza/vars/keyfiles/peerOrganizations/org.fpzornotza.com");
			CERT_PATH = CRYPTO_PATH.resolve(
					Paths.get("users/Admin@org.fpzornotza.com/msp/signcerts/Admin@org.fpzornotza.com-cert.pem"));
			KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/Admin@org.fpzornotza.com/msp/keystore"));
			TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer1.org.fpzornotza.com/tls/ca.crt"));
			PEER_ENDPOINT = "localhost:7002";
			OVERRIDE_AUTH = "peer1.org.fpzornotza.com";
			break;
		case 2:
			MSP_ID = "org-recymet-com";
			CRYPTO_PATH = Paths.get("/home/ekozir/recymet/vars/keyfiles/peerOrganizations/org.recymet.com");
			CERT_PATH = CRYPTO_PATH
					.resolve(Paths.get("users/Admin@org.recymet.com/msp/signcerts/Admin@org.recymet.com-cert.pem"));
			KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/Admin@org.recymet.com/msp/keystore"));
			TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer1.org.recymet.com/tls/ca.crt"));
			PEER_ENDPOINT = "localhost:7002";
			OVERRIDE_AUTH = "peer1.org.recymet.com";
			break;
		case 3:
			MSP_ID = "org-ormazabal-com";
			CRYPTO_PATH = Paths.get("/home/ekozir/ormazabal/vars/keyfiles/peerOrganizations/org.ormazabal.com");
			CERT_PATH = CRYPTO_PATH
					.resolve(Paths.get("users/Admin@org.ormazabal.com/msp/signcerts/Admin@org.ormazabal.com-cert.pem"));
			KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/Admin@org.ormazabal.com/msp/keystore"));
			TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer1.org.ormazabal.com/tls/ca.crt"));
			PEER_ENDPOINT = "localhost:7002";
			OVERRIDE_AUTH = "peer1.org.ormazabal.com";
			break;
		default:
			break;
		}

		if (opcion == 1 || opcion == 2 || opcion == 3) {
			// The gRPC client connection should be shared by all Gateway connections to
			// this endpoint.
			var channel = newGrpcConnection();
			var builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
					// Default timeouts for different gRPC calls
					.evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
					.endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
					.submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
					.commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
			System.out.println(builder.toString());
			try (var gateway = builder.connect()) {
				new AppEkozir(gateway).run(args);
			} finally {
				channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
			}
		}

		teclado.close();
	}

	private static ManagedChannel newGrpcConnection() throws IOException, CertificateException {
		var tlsCertReader = Files.newBufferedReader(TLS_CERT_PATH);
		var tlsCert = Identities.readX509Certificate(tlsCertReader);

		return NettyChannelBuilder.forTarget(PEER_ENDPOINT)
				.sslContext(GrpcSslContexts.forClient().trustManager(tlsCert).build()).overrideAuthority(OVERRIDE_AUTH)
				.build();
	}

	private static Identity newIdentity() throws IOException, CertificateException {
		var certReader = Files.newBufferedReader(CERT_PATH);
		var certificate = Identities.readX509Certificate(certReader);

		return new X509Identity(MSP_ID, certificate);
	}

	private static Signer newSigner() throws IOException, InvalidKeyException {
		var keyReader = Files.newBufferedReader(getPrivateKeyPath());
		var privateKey = Identities.readPrivateKey(keyReader);

		return Signers.newPrivateKeySigner(privateKey);
	}

	private static Path getPrivateKeyPath() throws IOException {
		try (var keyFiles = Files.list(KEY_DIR_PATH)) {
			return keyFiles.findFirst().orElseThrow();
		}
	}

	public AppEkozir(final Gateway gateway) {
		// Get a network instance representing the channel where the smart contract is
		// deployed.
		var network = gateway.getNetwork(CHANNEL_NAME);
		// Get the smart contract from the network.
		contract = network.getContract(CHAINCODE_NAME);
	}

	public void run(final String[] args) throws GatewayException, CommitException {

		int opcion = 0;

		while (opcion != 5) {
			System.out.println("");
			System.out.println("1. Obtener todos los registros.");
			System.out.println("2. Crear registro.");
			System.out.println("3. Leer registro.");
			System.out.println("4. Actualizar registro (sin implementar)");
			System.out.println("5. SALIR");
			System.out.println("");
			System.out.print("Selecciona opción: ");
			opcion = teclado.nextInt();

			switch (opcion) {
			case 1:
				getAllAssets();
				break;
			case 2:
				// TODO
				createAsset(String.valueOf(Instant.now().toEpochMilli()), "materiala", "Recymet", "FP Zornotza", "13",
						"burdina", "20 Kg", Instant.now().toString());
				break;
			case 3:
				System.out.print("id: ");
				readAssetById(teclado.next());
				break;
			case 4:
				// TODO
				System.out.println("Sin implementar");
				// Update an asset which does not exist.
				// updateNonExistentAsset();
				break;
			default:
				break;
			}
		}

		// Update an existing asset asynchronously.
		// transferAssetAsync();

	}

	/**
	 * This type of transaction would typically only be run once by an application
	 * the first time it was started after its initial deployment. A new version of
	 * the chaincode deployed later would likely not need to run an "init" function.
	 */
	private void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
		System.out.println(
				"\n--> Submit Transaction: InitLedger, function creates the initial set of assets on the ledger");

		contract.submitTransaction("InitLedger");

		System.out.println("*** Transaction committed successfully");
	}

	/**
	 * Evaluate a transaction to query ledger state.
	 */
	private void getAllAssets() throws GatewayException {
		System.out.println(
				"\n--> Evaluate Transaction: GetAllAssets, function returns all the current assets on the ledger");

		var result = contract.evaluateTransaction("GetAllAssets");

		System.out.println("*** Result: " + prettyJson(result));
	}

	private String prettyJson(final byte[] json) {
		return prettyJson(new String(json, StandardCharsets.UTF_8));
	}

	private String prettyJson(final String json) {
		var parsedJson = JsonParser.parseString(json);
		return gson.toJson(parsedJson);
	}

	/**
	 * Submit a transaction synchronously, blocking until it has been committed to
	 * the ledger.
	 */
	private void createAsset(final String id, final String tipo, final String origen, final String destino,
			final String lote, final String material, final String peso, final String fecha)
			throws EndorseException, SubmitException, CommitStatusException, CommitException {
		System.out.println("\n--> Submit Transaction: CreateAsset, creates new asset");

		contract.submitTransaction("CreateAsset", id, tipo, origen, destino, lote, material, peso, fecha);

		System.out.println("*** Transaction committed successfully");
	}

	/*
	 * Submit transaction asynchronously, allowing the application to process the
	 * smart contract response (e.g. update a UI) while waiting for the commit
	 * notification.
	 * 
	 * /* private void transferAssetAsync() throws EndorseException,
	 * SubmitException, CommitStatusException { System.out.
	 * println("\n--> Async Submit Transaction: TransferAsset, updates existing asset owner"
	 * );
	 * 
	 * var commit = contract.newProposal("TransferAsset") .addArguments(assetId,
	 * "Saptha") .build() .endorse() .submitAsync();
	 * 
	 * var result = commit.getResult(); var oldOwner = new String(result,
	 * StandardCharsets.UTF_8);
	 * 
	 * System.out.
	 * println("*** Successfully submitted transaction to transfer ownership from "
	 * + oldOwner + " to Saptha");
	 * System.out.println("*** Waiting for transaction commit");
	 * 
	 * var status = commit.getStatus(); if (!status.isSuccessful()) { throw new
	 * RuntimeException("Transaction " + status.getTransactionId() +
	 * " failed to commit with status code " + status.getCode()); }
	 * 
	 * System.out.println("*** Transaction committed successfully"); }
	 */
	private void readAssetById(String id) throws GatewayException {
		System.out.println("\n--> Evaluate Transaction: ReadAsset, function returns asset attributes");

		var evaluateResult = contract.evaluateTransaction("ReadAsset", id);

		System.out.println("*** Result:" + prettyJson(evaluateResult));
	}

	/**
	 * submitTransaction() will throw an error containing details of any error
	 * responses from the smart contract.
	 */
	private void updateNonExistentAsset() {
		try {
			System.out.println(
					"\n--> Submit Transaction: UpdateAsset asset70, asset70 does not exist and should return an error");

			contract.submitTransaction("UpdateAsset", "asset", "materiala", "Recymet", "FP Zornotza", "13", "burdina",
					"20 Kg", Instant.now().toString());

			System.out.println("******** FAILED to return an error");
		} catch (EndorseException | SubmitException | CommitStatusException e) {
			System.out.println("*** Successfully caught the error: ");
			e.printStackTrace(System.out);
			System.out.println("Transaction ID: " + e.getTransactionId());

			var details = e.getDetails();
			if (!details.isEmpty()) {
				System.out.println("Error Details:");
				for (var detail : details) {
					System.out.println("- address: " + detail.getAddress() + ", mspId: " + detail.getMspId()
							+ ", message: " + detail.getMessage());
				}
			}
		} catch (CommitException e) {
			System.out.println("*** Successfully caught the error: " + e);
			e.printStackTrace(System.out);
			System.out.println("Transaction ID: " + e.getTransactionId());
			System.out.println("Status code: " + e.getCode());
		}
	}
}
