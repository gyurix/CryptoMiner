// $Id: SHAvite256.java 222 2010-06-09 10:47:13Z tp $

package gyurix.cryptominer.cryptohash;

/**
 * <p>This class implements the SHAvite-256 digest algorithm under the
 * {@link Digest} API (in the SHAvite-3 specification, this function
 * is known as "SHAvite-3 with a 256-bit output").</p>
 * <p>
 * <pre>
 * ==========================(LICENSE BEGIN)============================
 *
 * Copyright (c) 2007-2010  Projet RNRT SAPHIR
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * ===========================(LICENSE END)=============================
 * </pre>
 *
 * @author Thomas Pornin &lt;thomas.pornin@cryptolog.com&gt;
 * @version $Revision: 222 $
 */

public class SHAvite256 extends SHAviteSmallCore {

    /**
     * The initial value for SHAvite-256.
     */
    private static final int[] initVal = {
            0x49BB3E47, 0x2674860D, 0xA8B392AC, 0x021AC4E6,
            0x409283CF, 0x620E5D86, 0x6D929DCB, 0x96CC2A8B
    };

    /**
     * Create the engine.
     */
    public SHAvite256() {
        super();
    }

    /**
     * @see Digest
     */
    public Digest copy() {
        return copyState(new SHAvite256());
    }

    /**
     * @see Digest
     */
    public int getDigestLength() {
        return 32;
    }

    /**
     * @see SHAviteSmallCore
     */
    int[] getInitVal() {
        return initVal;
    }
}
