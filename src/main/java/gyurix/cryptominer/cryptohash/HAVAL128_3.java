// $Id: HAVAL128_3.java 156 2010-04-26 17:55:11Z tp $

package gyurix.cryptominer.cryptohash;

/**
 * This class implements HAVAL with 128-bit output and 3 passes.
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
 * @version $Revision: 156 $
 */

public class HAVAL128_3 extends HAVALCore {

    /**
     * Create the object.
     */
    public HAVAL128_3() {
        super(128, 3);
    }

    /**
     * @see Digest
     */
    public Digest copy() {
        return copyState(new HAVAL128_3());
    }

    /**
     * @see Digest
     */
    public int getDigestLength() {
        return 16;
    }
}
