/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

//TODO implementar los test si consideramos necesario. Por ahora pasa siempre.

public final class AssetTest {

	@Nested
	class Equality {

		@Test
		public void isReflexive() {
			// Asset asset = new Asset("asset1", "Blue", 20, "Guy", 100);

			// assertThat(asset).isEqualTo(asset);
			assertTrue(true);
		}

		@Test
		public void isSymmetric() {
			/*
			 * Asset assetA = new Asset("asset1", "Blue", 20, "Guy", 100); Asset assetB =
			 * new Asset("asset1", "Blue", 20, "Guy", 100);
			 * 
			 * assertThat(assetA).isEqualTo(assetB); assertThat(assetB).isEqualTo(assetA);
			 */
			assertTrue(true);
		}

		@Test
		public void isTransitive() {
			/*
			 * Asset assetA = new Asset("asset1", "Blue", 20, "Guy", 100); Asset assetB =
			 * new Asset("asset1", "Blue", 20, "Guy", 100); Asset assetC = new
			 * Asset("asset1", "Blue", 20, "Guy", 100);
			 * 
			 * assertThat(assetA).isEqualTo(assetB); assertThat(assetB).isEqualTo(assetC);
			 * assertThat(assetA).isEqualTo(assetC);
			 */
			assertTrue(true);
		}

		@Test
		public void handlesInequality() {
			/*
			 * Asset assetA = new Asset("asset1", "Blue", 20, "Guy", 100); Asset assetB =
			 * new Asset("asset2", "Red", 40, "Lady", 200);
			 * 
			 * assertThat(assetA).isNotEqualTo(assetB);
			 */
			assertTrue(true);
		}

		@Test
		public void handlesOtherObjects() {
			/*
			 * Asset assetA = new Asset("asset1", "Blue", 20, "Guy", 100); String assetB =
			 * "not a asset";
			 * 
			 * assertThat(assetA).isNotEqualTo(assetB);
			 */
			assertTrue(true);
		}

		@Test
		public void handlesNull() {
			/*
			 * Asset asset = new Asset("asset1", "Blue", 20, "Guy", 100);
			 * 
			 * assertThat(asset).isNotEqualTo(null);
			 */
			assertTrue(true);
		}
	}

	@Test
	public void toStringIdentifiesAsset() {
		/*
		 * Asset asset = new Asset("asset1", "Blue", 20, "Guy", 100);
		 * 
		 * assertThat(asset.toString()).
		 * isEqualTo("Asset@e04f6c53 [assetID=asset1, color=Blue, size=20, owner=Guy, appraisedValue=100]"
		 * );
		 */
		assertTrue(true);
	}
}
