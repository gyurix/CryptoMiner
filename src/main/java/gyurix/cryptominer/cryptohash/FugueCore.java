// $Id: FugueCore.java 214 2010-06-03 17:25:08Z tp $

package gyurix.cryptominer.cryptohash;

/**
 * This class is the base class for Fugue implementation. It does not
 * use {@link DigestEngine} since Fugue is not nominally block-based.
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
 * @version $Revision: 214 $
 */

abstract class FugueCore implements Digest {

    static final int[] mixtab0 = {
            0x63633297, 0x7c7c6feb, 0x77775ec7, 0x7b7b7af7,
            0xf2f2e8e5, 0x6b6b0ab7, 0x6f6f16a7, 0xc5c56d39,
            0x303090c0, 0x01010704, 0x67672e87, 0x2b2bd1ac,
            0xfefeccd5, 0xd7d71371, 0xabab7c9a, 0x767659c3,
            0xcaca4005, 0x8282a33e, 0xc9c94909, 0x7d7d68ef,
            0xfafad0c5, 0x5959947f, 0x4747ce07, 0xf0f0e6ed,
            0xadad6e82, 0xd4d41a7d, 0xa2a243be, 0xafaf608a,
            0x9c9cf946, 0xa4a451a6, 0x727245d3, 0xc0c0762d,
            0xb7b728ea, 0xfdfdc5d9, 0x9393d47a, 0x2626f298,
            0x363682d8, 0x3f3fbdfc, 0xf7f7f3f1, 0xcccc521d,
            0x34348cd0, 0xa5a556a2, 0xe5e58db9, 0xf1f1e1e9,
            0x71714cdf, 0xd8d83e4d, 0x313197c4, 0x15156b54,
            0x04041c10, 0xc7c76331, 0x2323e98c, 0xc3c37f21,
            0x18184860, 0x9696cf6e, 0x05051b14, 0x9a9aeb5e,
            0x0707151c, 0x12127e48, 0x8080ad36, 0xe2e298a5,
            0xebeba781, 0x2727f59c, 0xb2b233fe, 0x757550cf,
            0x09093f24, 0x8383a43a, 0x2c2cc4b0, 0x1a1a4668,
            0x1b1b416c, 0x6e6e11a3, 0x5a5a9d73, 0xa0a04db6,
            0x5252a553, 0x3b3ba1ec, 0xd6d61475, 0xb3b334fa,
            0x2929dfa4, 0xe3e39fa1, 0x2f2fcdbc, 0x8484b126,
            0x5353a257, 0xd1d10169, 0x00000000, 0xededb599,
            0x2020e080, 0xfcfcc2dd, 0xb1b13af2, 0x5b5b9a77,
            0x6a6a0db3, 0xcbcb4701, 0xbebe17ce, 0x3939afe4,
            0x4a4aed33, 0x4c4cff2b, 0x5858937b, 0xcfcf5b11,
            0xd0d0066d, 0xefefbb91, 0xaaaa7b9e, 0xfbfbd7c1,
            0x4343d217, 0x4d4df82f, 0x333399cc, 0x8585b622,
            0x4545c00f, 0xf9f9d9c9, 0x02020e08, 0x7f7f66e7,
            0x5050ab5b, 0x3c3cb4f0, 0x9f9ff04a, 0xa8a87596,
            0x5151ac5f, 0xa3a344ba, 0x4040db1b, 0x8f8f800a,
            0x9292d37e, 0x9d9dfe42, 0x3838a8e0, 0xf5f5fdf9,
            0xbcbc19c6, 0xb6b62fee, 0xdada3045, 0x2121e784,
            0x10107040, 0xffffcbd1, 0xf3f3efe1, 0xd2d20865,
            0xcdcd5519, 0x0c0c2430, 0x1313794c, 0xececb29d,
            0x5f5f8667, 0x9797c86a, 0x4444c70b, 0x1717655c,
            0xc4c46a3d, 0xa7a758aa, 0x7e7e61e3, 0x3d3db3f4,
            0x6464278b, 0x5d5d886f, 0x19194f64, 0x737342d7,
            0x60603b9b, 0x8181aa32, 0x4f4ff627, 0xdcdc225d,
            0x2222ee88, 0x2a2ad6a8, 0x9090dd76, 0x88889516,
            0x4646c903, 0xeeeebc95, 0xb8b805d6, 0x14146c50,
            0xdede2c55, 0x5e5e8163, 0x0b0b312c, 0xdbdb3741,
            0xe0e096ad, 0x32329ec8, 0x3a3aa6e8, 0x0a0a3628,
            0x4949e43f, 0x06061218, 0x2424fc90, 0x5c5c8f6b,
            0xc2c27825, 0xd3d30f61, 0xacac6986, 0x62623593,
            0x9191da72, 0x9595c662, 0xe4e48abd, 0x797974ff,
            0xe7e783b1, 0xc8c84e0d, 0x373785dc, 0x6d6d18af,
            0x8d8d8e02, 0xd5d51d79, 0x4e4ef123, 0xa9a97292,
            0x6c6c1fab, 0x5656b943, 0xf4f4fafd, 0xeaeaa085,
            0x6565208f, 0x7a7a7df3, 0xaeae678e, 0x08083820,
            0xbaba0bde, 0x787873fb, 0x2525fb94, 0x2e2ecab8,
            0x1c1c5470, 0xa6a65fae, 0xb4b421e6, 0xc6c66435,
            0xe8e8ae8d, 0xdddd2559, 0x747457cb, 0x1f1f5d7c,
            0x4b4bea37, 0xbdbd1ec2, 0x8b8b9c1a, 0x8a8a9b1e,
            0x70704bdb, 0x3e3ebaf8, 0xb5b526e2, 0x66662983,
            0x4848e33b, 0x0303090c, 0xf6f6f4f5, 0x0e0e2a38,
            0x61613c9f, 0x35358bd4, 0x5757be47, 0xb9b902d2,
            0x8686bf2e, 0xc1c17129, 0x1d1d5374, 0x9e9ef74e,
            0xe1e191a9, 0xf8f8decd, 0x9898e556, 0x11117744,
            0x696904bf, 0xd9d93949, 0x8e8e870e, 0x9494c166,
            0x9b9bec5a, 0x1e1e5a78, 0x8787b82a, 0xe9e9a989,
            0xcece5c15, 0x5555b04f, 0x2828d8a0, 0xdfdf2b51,
            0x8c8c8906, 0xa1a14ab2, 0x89899212, 0x0d0d2334,
            0xbfbf10ca, 0xe6e684b5, 0x4242d513, 0x686803bb,
            0x4141dc1f, 0x9999e252, 0x2d2dc3b4, 0x0f0f2d3c,
            0xb0b03df6, 0x5454b74b, 0xbbbb0cda, 0x16166258
    };
    static final int[] mixtab1 = {
            0x97636332, 0xeb7c7c6f, 0xc777775e, 0xf77b7b7a,
            0xe5f2f2e8, 0xb76b6b0a, 0xa76f6f16, 0x39c5c56d,
            0xc0303090, 0x04010107, 0x8767672e, 0xac2b2bd1,
            0xd5fefecc, 0x71d7d713, 0x9aabab7c, 0xc3767659,
            0x05caca40, 0x3e8282a3, 0x09c9c949, 0xef7d7d68,
            0xc5fafad0, 0x7f595994, 0x074747ce, 0xedf0f0e6,
            0x82adad6e, 0x7dd4d41a, 0xbea2a243, 0x8aafaf60,
            0x469c9cf9, 0xa6a4a451, 0xd3727245, 0x2dc0c076,
            0xeab7b728, 0xd9fdfdc5, 0x7a9393d4, 0x982626f2,
            0xd8363682, 0xfc3f3fbd, 0xf1f7f7f3, 0x1dcccc52,
            0xd034348c, 0xa2a5a556, 0xb9e5e58d, 0xe9f1f1e1,
            0xdf71714c, 0x4dd8d83e, 0xc4313197, 0x5415156b,
            0x1004041c, 0x31c7c763, 0x8c2323e9, 0x21c3c37f,
            0x60181848, 0x6e9696cf, 0x1405051b, 0x5e9a9aeb,
            0x1c070715, 0x4812127e, 0x368080ad, 0xa5e2e298,
            0x81ebeba7, 0x9c2727f5, 0xfeb2b233, 0xcf757550,
            0x2409093f, 0x3a8383a4, 0xb02c2cc4, 0x681a1a46,
            0x6c1b1b41, 0xa36e6e11, 0x735a5a9d, 0xb6a0a04d,
            0x535252a5, 0xec3b3ba1, 0x75d6d614, 0xfab3b334,
            0xa42929df, 0xa1e3e39f, 0xbc2f2fcd, 0x268484b1,
            0x575353a2, 0x69d1d101, 0x00000000, 0x99ededb5,
            0x802020e0, 0xddfcfcc2, 0xf2b1b13a, 0x775b5b9a,
            0xb36a6a0d, 0x01cbcb47, 0xcebebe17, 0xe43939af,
            0x334a4aed, 0x2b4c4cff, 0x7b585893, 0x11cfcf5b,
            0x6dd0d006, 0x91efefbb, 0x9eaaaa7b, 0xc1fbfbd7,
            0x174343d2, 0x2f4d4df8, 0xcc333399, 0x228585b6,
            0x0f4545c0, 0xc9f9f9d9, 0x0802020e, 0xe77f7f66,
            0x5b5050ab, 0xf03c3cb4, 0x4a9f9ff0, 0x96a8a875,
            0x5f5151ac, 0xbaa3a344, 0x1b4040db, 0x0a8f8f80,
            0x7e9292d3, 0x429d9dfe, 0xe03838a8, 0xf9f5f5fd,
            0xc6bcbc19, 0xeeb6b62f, 0x45dada30, 0x842121e7,
            0x40101070, 0xd1ffffcb, 0xe1f3f3ef, 0x65d2d208,
            0x19cdcd55, 0x300c0c24, 0x4c131379, 0x9dececb2,
            0x675f5f86, 0x6a9797c8, 0x0b4444c7, 0x5c171765,
            0x3dc4c46a, 0xaaa7a758, 0xe37e7e61, 0xf43d3db3,
            0x8b646427, 0x6f5d5d88, 0x6419194f, 0xd7737342,
            0x9b60603b, 0x328181aa, 0x274f4ff6, 0x5ddcdc22,
            0x882222ee, 0xa82a2ad6, 0x769090dd, 0x16888895,
            0x034646c9, 0x95eeeebc, 0xd6b8b805, 0x5014146c,
            0x55dede2c, 0x635e5e81, 0x2c0b0b31, 0x41dbdb37,
            0xade0e096, 0xc832329e, 0xe83a3aa6, 0x280a0a36,
            0x3f4949e4, 0x18060612, 0x902424fc, 0x6b5c5c8f,
            0x25c2c278, 0x61d3d30f, 0x86acac69, 0x93626235,
            0x729191da, 0x629595c6, 0xbde4e48a, 0xff797974,
            0xb1e7e783, 0x0dc8c84e, 0xdc373785, 0xaf6d6d18,
            0x028d8d8e, 0x79d5d51d, 0x234e4ef1, 0x92a9a972,
            0xab6c6c1f, 0x435656b9, 0xfdf4f4fa, 0x85eaeaa0,
            0x8f656520, 0xf37a7a7d, 0x8eaeae67, 0x20080838,
            0xdebaba0b, 0xfb787873, 0x942525fb, 0xb82e2eca,
            0x701c1c54, 0xaea6a65f, 0xe6b4b421, 0x35c6c664,
            0x8de8e8ae, 0x59dddd25, 0xcb747457, 0x7c1f1f5d,
            0x374b4bea, 0xc2bdbd1e, 0x1a8b8b9c, 0x1e8a8a9b,
            0xdb70704b, 0xf83e3eba, 0xe2b5b526, 0x83666629,
            0x3b4848e3, 0x0c030309, 0xf5f6f6f4, 0x380e0e2a,
            0x9f61613c, 0xd435358b, 0x475757be, 0xd2b9b902,
            0x2e8686bf, 0x29c1c171, 0x741d1d53, 0x4e9e9ef7,
            0xa9e1e191, 0xcdf8f8de, 0x569898e5, 0x44111177,
            0xbf696904, 0x49d9d939, 0x0e8e8e87, 0x669494c1,
            0x5a9b9bec, 0x781e1e5a, 0x2a8787b8, 0x89e9e9a9,
            0x15cece5c, 0x4f5555b0, 0xa02828d8, 0x51dfdf2b,
            0x068c8c89, 0xb2a1a14a, 0x12898992, 0x340d0d23,
            0xcabfbf10, 0xb5e6e684, 0x134242d5, 0xbb686803,
            0x1f4141dc, 0x529999e2, 0xb42d2dc3, 0x3c0f0f2d,
            0xf6b0b03d, 0x4b5454b7, 0xdabbbb0c, 0x58161662
    };
    static final int[] mixtab2 = {
            0x32976363, 0x6feb7c7c, 0x5ec77777, 0x7af77b7b,
            0xe8e5f2f2, 0x0ab76b6b, 0x16a76f6f, 0x6d39c5c5,
            0x90c03030, 0x07040101, 0x2e876767, 0xd1ac2b2b,
            0xccd5fefe, 0x1371d7d7, 0x7c9aabab, 0x59c37676,
            0x4005caca, 0xa33e8282, 0x4909c9c9, 0x68ef7d7d,
            0xd0c5fafa, 0x947f5959, 0xce074747, 0xe6edf0f0,
            0x6e82adad, 0x1a7dd4d4, 0x43bea2a2, 0x608aafaf,
            0xf9469c9c, 0x51a6a4a4, 0x45d37272, 0x762dc0c0,
            0x28eab7b7, 0xc5d9fdfd, 0xd47a9393, 0xf2982626,
            0x82d83636, 0xbdfc3f3f, 0xf3f1f7f7, 0x521dcccc,
            0x8cd03434, 0x56a2a5a5, 0x8db9e5e5, 0xe1e9f1f1,
            0x4cdf7171, 0x3e4dd8d8, 0x97c43131, 0x6b541515,
            0x1c100404, 0x6331c7c7, 0xe98c2323, 0x7f21c3c3,
            0x48601818, 0xcf6e9696, 0x1b140505, 0xeb5e9a9a,
            0x151c0707, 0x7e481212, 0xad368080, 0x98a5e2e2,
            0xa781ebeb, 0xf59c2727, 0x33feb2b2, 0x50cf7575,
            0x3f240909, 0xa43a8383, 0xc4b02c2c, 0x46681a1a,
            0x416c1b1b, 0x11a36e6e, 0x9d735a5a, 0x4db6a0a0,
            0xa5535252, 0xa1ec3b3b, 0x1475d6d6, 0x34fab3b3,
            0xdfa42929, 0x9fa1e3e3, 0xcdbc2f2f, 0xb1268484,
            0xa2575353, 0x0169d1d1, 0x00000000, 0xb599eded,
            0xe0802020, 0xc2ddfcfc, 0x3af2b1b1, 0x9a775b5b,
            0x0db36a6a, 0x4701cbcb, 0x17cebebe, 0xafe43939,
            0xed334a4a, 0xff2b4c4c, 0x937b5858, 0x5b11cfcf,
            0x066dd0d0, 0xbb91efef, 0x7b9eaaaa, 0xd7c1fbfb,
            0xd2174343, 0xf82f4d4d, 0x99cc3333, 0xb6228585,
            0xc00f4545, 0xd9c9f9f9, 0x0e080202, 0x66e77f7f,
            0xab5b5050, 0xb4f03c3c, 0xf04a9f9f, 0x7596a8a8,
            0xac5f5151, 0x44baa3a3, 0xdb1b4040, 0x800a8f8f,
            0xd37e9292, 0xfe429d9d, 0xa8e03838, 0xfdf9f5f5,
            0x19c6bcbc, 0x2feeb6b6, 0x3045dada, 0xe7842121,
            0x70401010, 0xcbd1ffff, 0xefe1f3f3, 0x0865d2d2,
            0x5519cdcd, 0x24300c0c, 0x794c1313, 0xb29decec,
            0x86675f5f, 0xc86a9797, 0xc70b4444, 0x655c1717,
            0x6a3dc4c4, 0x58aaa7a7, 0x61e37e7e, 0xb3f43d3d,
            0x278b6464, 0x886f5d5d, 0x4f641919, 0x42d77373,
            0x3b9b6060, 0xaa328181, 0xf6274f4f, 0x225ddcdc,
            0xee882222, 0xd6a82a2a, 0xdd769090, 0x95168888,
            0xc9034646, 0xbc95eeee, 0x05d6b8b8, 0x6c501414,
            0x2c55dede, 0x81635e5e, 0x312c0b0b, 0x3741dbdb,
            0x96ade0e0, 0x9ec83232, 0xa6e83a3a, 0x36280a0a,
            0xe43f4949, 0x12180606, 0xfc902424, 0x8f6b5c5c,
            0x7825c2c2, 0x0f61d3d3, 0x6986acac, 0x35936262,
            0xda729191, 0xc6629595, 0x8abde4e4, 0x74ff7979,
            0x83b1e7e7, 0x4e0dc8c8, 0x85dc3737, 0x18af6d6d,
            0x8e028d8d, 0x1d79d5d5, 0xf1234e4e, 0x7292a9a9,
            0x1fab6c6c, 0xb9435656, 0xfafdf4f4, 0xa085eaea,
            0x208f6565, 0x7df37a7a, 0x678eaeae, 0x38200808,
            0x0bdebaba, 0x73fb7878, 0xfb942525, 0xcab82e2e,
            0x54701c1c, 0x5faea6a6, 0x21e6b4b4, 0x6435c6c6,
            0xae8de8e8, 0x2559dddd, 0x57cb7474, 0x5d7c1f1f,
            0xea374b4b, 0x1ec2bdbd, 0x9c1a8b8b, 0x9b1e8a8a,
            0x4bdb7070, 0xbaf83e3e, 0x26e2b5b5, 0x29836666,
            0xe33b4848, 0x090c0303, 0xf4f5f6f6, 0x2a380e0e,
            0x3c9f6161, 0x8bd43535, 0xbe475757, 0x02d2b9b9,
            0xbf2e8686, 0x7129c1c1, 0x53741d1d, 0xf74e9e9e,
            0x91a9e1e1, 0xdecdf8f8, 0xe5569898, 0x77441111,
            0x04bf6969, 0x3949d9d9, 0x870e8e8e, 0xc1669494,
            0xec5a9b9b, 0x5a781e1e, 0xb82a8787, 0xa989e9e9,
            0x5c15cece, 0xb04f5555, 0xd8a02828, 0x2b51dfdf,
            0x89068c8c, 0x4ab2a1a1, 0x92128989, 0x23340d0d,
            0x10cabfbf, 0x84b5e6e6, 0xd5134242, 0x03bb6868,
            0xdc1f4141, 0xe2529999, 0xc3b42d2d, 0x2d3c0f0f,
            0x3df6b0b0, 0xb74b5454, 0x0cdabbbb, 0x62581616
    };
    static final int[] mixtab3 = {
            0x63329763, 0x7c6feb7c, 0x775ec777, 0x7b7af77b,
            0xf2e8e5f2, 0x6b0ab76b, 0x6f16a76f, 0xc56d39c5,
            0x3090c030, 0x01070401, 0x672e8767, 0x2bd1ac2b,
            0xfeccd5fe, 0xd71371d7, 0xab7c9aab, 0x7659c376,
            0xca4005ca, 0x82a33e82, 0xc94909c9, 0x7d68ef7d,
            0xfad0c5fa, 0x59947f59, 0x47ce0747, 0xf0e6edf0,
            0xad6e82ad, 0xd41a7dd4, 0xa243bea2, 0xaf608aaf,
            0x9cf9469c, 0xa451a6a4, 0x7245d372, 0xc0762dc0,
            0xb728eab7, 0xfdc5d9fd, 0x93d47a93, 0x26f29826,
            0x3682d836, 0x3fbdfc3f, 0xf7f3f1f7, 0xcc521dcc,
            0x348cd034, 0xa556a2a5, 0xe58db9e5, 0xf1e1e9f1,
            0x714cdf71, 0xd83e4dd8, 0x3197c431, 0x156b5415,
            0x041c1004, 0xc76331c7, 0x23e98c23, 0xc37f21c3,
            0x18486018, 0x96cf6e96, 0x051b1405, 0x9aeb5e9a,
            0x07151c07, 0x127e4812, 0x80ad3680, 0xe298a5e2,
            0xeba781eb, 0x27f59c27, 0xb233feb2, 0x7550cf75,
            0x093f2409, 0x83a43a83, 0x2cc4b02c, 0x1a46681a,
            0x1b416c1b, 0x6e11a36e, 0x5a9d735a, 0xa04db6a0,
            0x52a55352, 0x3ba1ec3b, 0xd61475d6, 0xb334fab3,
            0x29dfa429, 0xe39fa1e3, 0x2fcdbc2f, 0x84b12684,
            0x53a25753, 0xd10169d1, 0x00000000, 0xedb599ed,
            0x20e08020, 0xfcc2ddfc, 0xb13af2b1, 0x5b9a775b,
            0x6a0db36a, 0xcb4701cb, 0xbe17cebe, 0x39afe439,
            0x4aed334a, 0x4cff2b4c, 0x58937b58, 0xcf5b11cf,
            0xd0066dd0, 0xefbb91ef, 0xaa7b9eaa, 0xfbd7c1fb,
            0x43d21743, 0x4df82f4d, 0x3399cc33, 0x85b62285,
            0x45c00f45, 0xf9d9c9f9, 0x020e0802, 0x7f66e77f,
            0x50ab5b50, 0x3cb4f03c, 0x9ff04a9f, 0xa87596a8,
            0x51ac5f51, 0xa344baa3, 0x40db1b40, 0x8f800a8f,
            0x92d37e92, 0x9dfe429d, 0x38a8e038, 0xf5fdf9f5,
            0xbc19c6bc, 0xb62feeb6, 0xda3045da, 0x21e78421,
            0x10704010, 0xffcbd1ff, 0xf3efe1f3, 0xd20865d2,
            0xcd5519cd, 0x0c24300c, 0x13794c13, 0xecb29dec,
            0x5f86675f, 0x97c86a97, 0x44c70b44, 0x17655c17,
            0xc46a3dc4, 0xa758aaa7, 0x7e61e37e, 0x3db3f43d,
            0x64278b64, 0x5d886f5d, 0x194f6419, 0x7342d773,
            0x603b9b60, 0x81aa3281, 0x4ff6274f, 0xdc225ddc,
            0x22ee8822, 0x2ad6a82a, 0x90dd7690, 0x88951688,
            0x46c90346, 0xeebc95ee, 0xb805d6b8, 0x146c5014,
            0xde2c55de, 0x5e81635e, 0x0b312c0b, 0xdb3741db,
            0xe096ade0, 0x329ec832, 0x3aa6e83a, 0x0a36280a,
            0x49e43f49, 0x06121806, 0x24fc9024, 0x5c8f6b5c,
            0xc27825c2, 0xd30f61d3, 0xac6986ac, 0x62359362,
            0x91da7291, 0x95c66295, 0xe48abde4, 0x7974ff79,
            0xe783b1e7, 0xc84e0dc8, 0x3785dc37, 0x6d18af6d,
            0x8d8e028d, 0xd51d79d5, 0x4ef1234e, 0xa97292a9,
            0x6c1fab6c, 0x56b94356, 0xf4fafdf4, 0xeaa085ea,
            0x65208f65, 0x7a7df37a, 0xae678eae, 0x08382008,
            0xba0bdeba, 0x7873fb78, 0x25fb9425, 0x2ecab82e,
            0x1c54701c, 0xa65faea6, 0xb421e6b4, 0xc66435c6,
            0xe8ae8de8, 0xdd2559dd, 0x7457cb74, 0x1f5d7c1f,
            0x4bea374b, 0xbd1ec2bd, 0x8b9c1a8b, 0x8a9b1e8a,
            0x704bdb70, 0x3ebaf83e, 0xb526e2b5, 0x66298366,
            0x48e33b48, 0x03090c03, 0xf6f4f5f6, 0x0e2a380e,
            0x613c9f61, 0x358bd435, 0x57be4757, 0xb902d2b9,
            0x86bf2e86, 0xc17129c1, 0x1d53741d, 0x9ef74e9e,
            0xe191a9e1, 0xf8decdf8, 0x98e55698, 0x11774411,
            0x6904bf69, 0xd93949d9, 0x8e870e8e, 0x94c16694,
            0x9bec5a9b, 0x1e5a781e, 0x87b82a87, 0xe9a989e9,
            0xce5c15ce, 0x55b04f55, 0x28d8a028, 0xdf2b51df,
            0x8c89068c, 0xa14ab2a1, 0x89921289, 0x0d23340d,
            0xbf10cabf, 0xe684b5e6, 0x42d51342, 0x6803bb68,
            0x41dc1f41, 0x99e25299, 0x2dc3b42d, 0x0f2d3c0f,
            0xb03df6b0, 0x54b74b54, 0xbb0cdabb, 0x16625816
    };
    int[] S, tmpS;
    int rshift;
    private long bitCount;
    private int partial, partialLen;
    private byte[] out;

    FugueCore() {
        out = new byte[getDigestLength()];
        S = new int[36];
        tmpS = new int[36];
        doReset();
    }

    static final void encodeBEInt(int val, byte[] buf, int off) {
        buf[off + 0] = (byte) (val >>> 24);
        buf[off + 1] = (byte) (val >>> 16);
        buf[off + 2] = (byte) (val >>> 8);
        buf[off + 3] = (byte) val;
    }

    final void cmix30() {
        int[] S = this.S;
        S[0] ^= S[4];
        S[1] ^= S[5];
        S[2] ^= S[6];
        S[15] ^= S[4];
        S[16] ^= S[5];
        S[17] ^= S[6];
    }

    final void cmix36() {
        int[] S = this.S;
        S[0] ^= S[4];
        S[1] ^= S[5];
        S[2] ^= S[6];
        S[18] ^= S[4];
        S[19] ^= S[5];
        S[20] ^= S[6];
    }

    /**
     * @see Digest
     */
    public Digest copy() {
        FugueCore fc = dup();
        fc.bitCount = bitCount;
        fc.partial = partial;
        fc.partialLen = partialLen;
        fc.rshift = rshift;
        System.arraycopy(S, 0, fc.S, 0, S.length);
        return fc;
    }

    /**
     * @see Digest
     */
    public byte[] digest() {
        int n = getDigestLength();
        byte[] out = new byte[n];
        digest(out, 0, n);
        return out;
    }

    /**
     * @see Digest
     */
    public byte[] digest(byte[] inbuf) {
        update(inbuf, 0, inbuf.length);
        return digest();
    }

    /**
     * @see Digest
     */
    public int digest(byte[] outbuf, int off, int len) {
        if (partialLen != 0) {
            while (partialLen++ < 4)
                partial <<= 8;
            process(partial);
        }
        process((int) (bitCount >>> 32));
        process((int) bitCount);
        processFinal(out);
        if (len > out.length)
            len = out.length;
        System.arraycopy(out, 0, outbuf, off, len);
        doReset();
        return len;
    }

    /**
     * @see Digest
     */
    public int getBlockLength() {
        /*
         * Private communication from Charanjit Jutla (one of
         * the Fugue designers):
         *
         * << we always set the parameter B (which is the number of
         *    bytes in ipad, opad) as B = 4*ceil(#-bits-in-key /32). >>
         */
        return -4;
    }

    /**
     * @see Digest
     */
    public void reset() {
        doReset();
    }

    /**
     * @see Digest
     */
    public void update(byte in) {
        bitCount += 8;
        partial = (partial << 8) | (in & 0xFF);
        if (++partialLen == 4) {
            process(partial);
            partial = 0;
            partialLen = 0;
        }
    }

    /**
     * @see Digest
     */
    public void update(byte[] inbuf) {
        update(inbuf, 0, inbuf.length);
    }

    /**
     * @see Digest
     */
    public void update(byte[] inbuf, int off, int len) {
        bitCount += (long) len << 3;
        while (partialLen < 4 && len > 0) {
            partial = (partial << 8) | (inbuf[off++] & 0xFF);
            partialLen++;
            len--;
        }
        if (partialLen == 4 || len > 0) {
            int zlen = len & ~3;
            process(partial, inbuf, off, zlen >>> 2);
            off += zlen;
            len -= zlen;
            partial = 0;
            partialLen = len;
            while (len-- > 0)
                partial = (partial << 8)
                        | (inbuf[off++] & 0xFF);
        }
    }

    private void doReset() {
        int[] iv = getIV();
        int zlen;
        if (getDigestLength() <= 32) {
            zlen = 30 - iv.length;
        } else {
            zlen = 36 - iv.length;
        }
        for (int i = 0; i < zlen; i++)
            S[i] = 0;
        System.arraycopy(iv, 0, S, zlen, iv.length);
        bitCount = 0;
        partial = 0;
        partialLen = 0;
        rshift = 0;
    }

    abstract FugueCore dup();

    abstract int[] getIV();

    /**
     * Process a single word.
     *
     * @param w the 32-bit word to process
     */
    private void process(int w) {
        process(w, null, 0, 0);
    }

    /**
     * Process some words. The first 32-bit word is {@code w}, then
     * there are {@code num} other words to be found in {@code buf},
     * starting at offset {@code off}
     */
    abstract void process(int w, byte[] buf, int off, int num);

    /**
     * Perform the final round.
     *
     * @param out the (temporary) output buffer
     */
    abstract void processFinal(byte[] out);

    final void ror(int rc, int len) {
        System.arraycopy(S, len - rc, tmpS, 0, rc);
        System.arraycopy(S, 0, S, rc, len - rc);
        System.arraycopy(tmpS, 0, S, 0, rc);
    }

    final void smix(int i0, int i1, int i2, int i3) {
        int[] S = this.S;
        int c0 = 0, c1 = 0, c2 = 0, c3 = 0;
        int r0 = 0, r1 = 0, r2 = 0, r3 = 0;
        int tmp, xt;
        xt = S[i0];
        tmp = mixtab0[(xt >>> 24) & 0xFF];
        c0 ^= tmp;
        tmp = mixtab1[(xt >>> 16) & 0xFF];
        c0 ^= tmp;
        r1 ^= tmp;
        tmp = mixtab2[(xt >>> 8) & 0xFF];
        c0 ^= tmp;
        r2 ^= tmp;
        tmp = mixtab3[(xt >>> 0) & 0xFF];
        c0 ^= tmp;
        r3 ^= tmp;
        xt = S[i1];
        tmp = mixtab0[(xt >>> 24) & 0xFF];
        c1 ^= tmp;
        r0 ^= tmp;
        tmp = mixtab1[(xt >>> 16) & 0xFF];
        c1 ^= tmp;
        tmp = mixtab2[(xt >>> 8) & 0xFF];
        c1 ^= tmp;
        r2 ^= tmp;
        tmp = mixtab3[(xt >>> 0) & 0xFF];
        c1 ^= tmp;
        r3 ^= tmp;
        xt = S[i2];
        tmp = mixtab0[(xt >>> 24) & 0xFF];
        c2 ^= tmp;
        r0 ^= tmp;
        tmp = mixtab1[(xt >>> 16) & 0xFF];
        c2 ^= tmp;
        r1 ^= tmp;
        tmp = mixtab2[(xt >>> 8) & 0xFF];
        c2 ^= tmp;
        tmp = mixtab3[(xt >>> 0) & 0xFF];
        c2 ^= tmp;
        r3 ^= tmp;
        xt = S[i3];
        tmp = mixtab0[(xt >>> 24) & 0xFF];
        c3 ^= tmp;
        r0 ^= tmp;
        tmp = mixtab1[(xt >>> 16) & 0xFF];
        c3 ^= tmp;
        r1 ^= tmp;
        tmp = mixtab2[(xt >>> 8) & 0xFF];
        c3 ^= tmp;
        r2 ^= tmp;
        tmp = mixtab3[(xt >>> 0) & 0xFF];
        c3 ^= tmp;
        S[i0] = ((c0 ^ (r0 << 0)) & 0xFF000000)
                | ((c1 ^ (r1 << 0)) & 0x00FF0000)
                | ((c2 ^ (r2 << 0)) & 0x0000FF00)
                | ((c3 ^ (r3 << 0)) & 0x000000FF);
        S[i1] = ((c1 ^ (r0 << 8)) & 0xFF000000)
                | ((c2 ^ (r1 << 8)) & 0x00FF0000)
                | ((c3 ^ (r2 << 8)) & 0x0000FF00)
                | ((c0 ^ (r3 >>> 24)) & 0x000000FF);
        S[i2] = ((c2 ^ (r0 << 16)) & 0xFF000000)
                | ((c3 ^ (r1 << 16)) & 0x00FF0000)
                | ((c0 ^ (r2 >>> 16)) & 0x0000FF00)
                | ((c1 ^ (r3 >>> 16)) & 0x000000FF);
        S[i3] = ((c3 ^ (r0 << 24)) & 0xFF000000)
                | ((c0 ^ (r1 >>> 8)) & 0x00FF0000)
                | ((c1 ^ (r2 >>> 8)) & 0x0000FF00)
                | ((c2 ^ (r3 >>> 8)) & 0x000000FF);
    }

    /**
     * @see Digest
     */
    public String toString() {
        return "Fugue-" + (getDigestLength() << 3);
    }
}
