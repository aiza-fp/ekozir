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

public final class AppMinifab {
	private static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");
	private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "channel1");
	private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "ekozir");

	// Path to crypto materials.
	private static final Path CRYPTO_PATH = Paths
			.get("/home/ekozir/mywork/vars/keyfiles/peerOrganizations/org1.ekozir.com");
	// Path to user certificate.
	private static final Path CERT_PATH = CRYPTO_PATH
			.resolve(Paths.get("users/Admin@org1.ekozir.com/msp/signcerts/Admin@org1.ekozir.com-cert.pem")); // cert.pem
	// Path to user private key directory.
	private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/Admin@org1.ekozir.com/msp/keystore"));
	// Path to peer tls certificate.
	private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer1.org1.ekozir.com/tls/ca.crt"));

	// Gateway peer end point.
	private static final String PEER_ENDPOINT = "localhost:7051";
	private static final String OVERRIDE_AUTH = "peer1.org1.ekozir.com";

	private final Contract contract;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	
	public static void main(final String[] args) throws Exception {
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
			new AppMinifab(gateway).run(args);
		} finally {
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}
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

	public AppMinifab(final Gateway gateway) {
		// Get a network instance representing the channel where the smart contract is
		// deployed.
		var network = gateway.getNetwork(CHANNEL_NAME);
		// Get the smart contract from the network.
		contract = network.getContract(CHAINCODE_NAME);
	}

	public void run(final String[] args) throws GatewayException, CommitException {
		// Initialize a set of asset data on the ledger using the chaincode 'InitLedger'
		// function.
		// initLedger();
		if (args != null && args.length > 0) {
			if ("getAllAssets".equals(args[0])) {
				// Return all the current assets on the ledger.
				getAllAssets();
			} else if ("createAsset".equals(args[0])) {
				// Create a new asset on the ledger.
				createAsset(String.valueOf(Instant.now().toEpochMilli()), args[0], args[0], args[0], args[0], args[0],
						args[0], Instant.now().toString());
			} else if ("readAsset".equals(args[0])) {
				// Get the asset details by assetID.
				readAssetById(args[1]);
			} else if ("updateAsset".equals(args[0])) {
				// Update an asset which does not exist.
				updateNonExistentAsset();
			}
		} else {
			int opcion = 0;
			Scanner teclado = new Scanner(System.in);
			while (opcion != 5) {
				System.out.println("");
				System.out.println("1. getAllAssets");
				System.out.println("2. createAsset");
				System.out.println("3. readAsset");
				System.out.println("4. updateAsset");
				System.out.println("5. SALIR");
				
				opcion = teclado.nextInt();

				switch (opcion) {
				case 1:
					getAllAssets();
					break;
				case 2:
					//TODO
					createAsset(String.valueOf(Instant.now().toEpochMilli()), "materiala", "Recymet", "FP Zornotza",
							"13", "burdina", "20 Kg", Instant.now().toString());
					break;
				case 3:
					System.out.print("id: ");					
					readAssetById(teclado.next());
					break;
				case 4:
					//TODO
					System.out.println("Sin implementar");
					break;
				default:
					break;
				}
			}
			teclado.close();
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
