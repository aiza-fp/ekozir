/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(name = "basic", info = @Info(title = "Asset Transfer", description = "The hyperlegendary asset transfer", version = "0.0.1-SNAPSHOT", license = @License(name = "Apache 2.0 License", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), contact = @Contact(email = "aiza@fpzornotza.com", name = "Ekozir Transfer", url = "")))
@Default
public final class AssetTransfer implements ContractInterface {

	private final Genson genson = new Genson();

	private enum AssetTransferErrors {
		ASSET_NOT_FOUND, ASSET_ALREADY_EXISTS
	}

	/**
	 * Creates some initial assets on the ledger.
	 *
	 * @param ctx the transaction context
	 */
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public void InitLedger(final Context ctx) {
		ChaincodeStub stub = ctx.getStub();
		Date fechaDate = new Date(System.currentTimeMillis());

		CreateAsset(ctx, "0", "tipo", "origen", "destino", "lote", "material", "peso", fechaDate.toString());

	}

	/**
	 * Crear registro en el ledger.
	 *
	 * @param ctx      the transaction context
	 * @param id
	 * @param tipo
	 * @param origen
	 * @param destino
	 * @param lote
	 * @param material
	 * @param peso
	 * @param fecha
	 * @return
	 */
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public Asset CreateAsset(final Context ctx, final String id, final String tipo, final String origen,
			final String destino, final String lote, final String material, final String peso, final String fecha) {
		ChaincodeStub stub = ctx.getStub();

		if (AssetExists(ctx, id)) {
			String errorMessage = String.format("Objeto %s ya existe", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
		}

		Asset asset = new Asset(id, tipo, origen, destino, lote, material, peso, fecha);
		// Use Genson to convert the Asset into string, sort it alphabetically and
		// serialize it into a json string
		String sortedJson = genson.serialize(asset);
		stub.putStringState(id, sortedJson);

		return asset;
	}

	/**
	 * Obtener un registro con el ID especificado.
	 *
	 * @param ctx the transaction context
	 * @param id  the ID of the asset
	 * @return the asset found on the ledger if there was one
	 */
	@Transaction(intent = Transaction.TYPE.EVALUATE)
	public Asset ReadAsset(final Context ctx, final String id) {
		ChaincodeStub stub = ctx.getStub();
		String assetJSON = stub.getStringState(id);

		if (assetJSON == null || assetJSON.isEmpty()) {
			String errorMessage = String.format("Asset %s does not exist", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
		}

		Asset asset = genson.deserialize(assetJSON, Asset.class);
		return asset;
	}

	/**
	 * Actualiza el contenido de un registro.
	 * 
	 * @param ctx		the transaction context
	 * @param id
	 * @param tipo
	 * @param origen
	 * @param destino
	 * @param lote
	 * @param material
	 * @param peso
	 * @param fecha
	 * @return
	 */
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public Asset UpdateAsset(final Context ctx, final String id, final String tipo, final String origen,
			final String destino, final String lote, final String material, final String peso, final String fecha) {
		ChaincodeStub stub = ctx.getStub();

		if (!AssetExists(ctx, id)) {
			String errorMessage = String.format("Objeto %s no existe", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
		}

		Asset newAsset = new Asset(id, tipo, origen, destino, lote, material, peso, fecha);
		// Use Genson to convert the Asset into string, sort it alphabetically and
		// serialize it into a json string
		String sortedJson = genson.serialize(newAsset);
		stub.putStringState(id, sortedJson);
		return newAsset;
	}

	/**
	 * Elimina un registro del ledger.
	 *
	 * @param ctx the transaction context
	 * @param id  the ID of the asset being deleted
	 */
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public void DeleteAsset(final Context ctx, final String id) {
		ChaincodeStub stub = ctx.getStub();

		if (!AssetExists(ctx, id)) {
			String errorMessage = String.format("Objeto %s no existe", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
		}

		stub.delState(id);
	}

	/**
	 * Comprueba la existencia de un registro en el ledger.
	 *
	 * @param ctx the transaction context
	 * @param id  the ID of the asset
	 * @return boolean indicating the existence of the asset
	 */
	@Transaction(intent = Transaction.TYPE.EVALUATE)
	public boolean AssetExists(final Context ctx, final String id) {
		ChaincodeStub stub = ctx.getStub();
		String assetJSON = stub.getStringState(id);

		return (assetJSON != null && !assetJSON.isEmpty());
	}

	/**
	 * Changes the owner of a asset on the ledger.
	 *
	 * @param ctx      the transaction context
	 * @param id       the ID of the asset being transferred
	 * @param newOwner the new owner
	 * @return the old owner
	 */
	/*
	 * @Transaction(intent = Transaction.TYPE.SUBMIT) public String
	 * TransferAsset(final Context ctx, final String id, final String newOwner) {
	 * ChaincodeStub stub = ctx.getStub(); String assetJSON =
	 * stub.getStringState(id);
	 * 
	 * if (assetJSON == null || assetJSON.isEmpty()) { String errorMessage =
	 * String.format("Asset %s does not exist", id);
	 * System.out.println(errorMessage); throw new ChaincodeException(errorMessage,
	 * AssetTransferErrors.ASSET_NOT_FOUND.toString()); }
	 * 
	 * Asset asset = genson.deserialize(assetJSON, Asset.class);
	 * 
	 * Asset newAsset = new Asset(asset.getId(), asset.getColor(), asset.getSize(),
	 * newOwner, asset.getLote()); // Use a Genson to conver the Asset into string,
	 * sort it alphabetically and serialize it into a json string String sortedJson
	 * = genson.serialize(newAsset); stub.putStringState(id, sortedJson);
	 * 
	 * return asset.getOwner(); }
	 */
	/**
	 * Retrieves all assets from the ledger.
	 *
	 * @param ctx the transaction context
	 * @return array of assets found on the ledger
	 */
	@Transaction(intent = Transaction.TYPE.EVALUATE)
	public String GetAllAssets(final Context ctx) {
		ChaincodeStub stub = ctx.getStub();

		List<Asset> queryResults = new ArrayList<Asset>();

		// To retrieve all assets from the ledger use getStateByRange with empty
		// startKey & endKey.
		// Giving empty startKey & endKey is interpreted as all the keys from beginning
		// to end.
		// As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
		// then getStateByRange will retrieve asset with keys between asset0 (inclusive)
		// and asset9 (exclusive) in lexical order.
		QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

		for (KeyValue result : results) {
			Asset asset = genson.deserialize(result.getStringValue(), Asset.class);
			System.out.println(asset);
			queryResults.add(asset);
		}

		final String response = genson.serialize(queryResults);

		return response;
	}
}
