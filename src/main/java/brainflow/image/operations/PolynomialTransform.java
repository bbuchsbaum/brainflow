/*
 * PolynomialTransform.java
 *
 * Created on May 20, 2003, 10:12 AM
 */

package brainflow.image.operations;

import brainflow.utils.Point3D;


/**
 * @author Bradley
 */
public class PolynomialTransform {

    public static final int MAX_ORDER = 12;

    public static final int MAX_COEFFP = 455;

    public static final int MAX_COORD = 3;

    public static final int MIN_COORD = 2;

    int order;

    int coeffp;

    int dimensionality;

    double[][] matrix;
    
    double[] dx;

    /**
     * Creates a new instance of PolynomialTransform
     */
    public PolynomialTransform() {
    }


    public double[][] getMatrix() {
        return matrix;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int _order) {
        order = _order;
        coeffp = (order + 1) * (order + 2) * (order + 3) / 6;
        dx = new double[coeffp];
    }

    public int getNumCoefficients() {
        return coeffp;
    }

    public void setMatrix(double[][] mat) {
        if (mat.length != 3) {
            throw new IllegalArgumentException("PolynomialTransform only implemented for 3D case");
        } else if (mat[0].length != coeffp) {
            throw new IllegalArgumentException("Number of coefficients does not match polynomial order");
        }

        matrix = mat;
    }


    public Point3D transformPoint(Point3D in, Point3D out) {
        double i1 = in.x;
        double j1 = in.y;
        double k1 = in.z;

        //double[] ncoords = new double[3];


        dx[0] = 1.0;
        if (order != 0) {
            dx[1] = i1;
            dx[2] = j1;
            dx[3] = k1;

            if (order > 1) {

                double i2 = i1 * i1;
                double j2 = j1 * j1;
                double k2 = k1 * k1;
                dx[4] = i2;
                dx[5] = i1 * j1;
                dx[6] = j2;
                dx[7] = i1 * k1;
                dx[8] = j1 * k1;
                dx[9] = k2;

                if (order > 2) {

                    double i3 = i2 * i1;
                    double j3 = j2 * j1;
                    double k3 = k2 * k1;

                    dx[10] = i3;
                    dx[11] = i2 * j1;
                    dx[12] = i1 * j2;
                    dx[13] = j3;
                    dx[14] = i2 * k1;
                    dx[15] = i1 * j1 * k1;
                    dx[16] = j2 * k1;
                    dx[17] = i1 * k2;
                    dx[18] = j1 * k2;
                    dx[19] = k3;

                    if (order > 3) {

                        double i4 = i3 * i1;
                        double j4 = j3 * j1;
                        double k4 = k3 * k1;

                        dx[20] = i4;
                        dx[21] = i3 * j1;
                        dx[22] = i2 * j2;
                        dx[23] = i1 * j3;
                        dx[24] = j4;
                        dx[25] = i3 * k1;
                        dx[26] = i2 * j1 * k1;
                        dx[27] = i1 * j2 * k1;
                        dx[28] = j3 * k1;
                        dx[29] = i2 * k2;
                        dx[30] = i1 * j1 * k2;
                        dx[31] = j2 * k2;
                        dx[32] = i1 * k3;
                        dx[33] = j1 * k3;
                        dx[34] = k4;

                        if (order > 4) {

                            double i5 = i4 * i1;
                            double j5 = j4 * j1;
                            double k5 = k4 * k1;

                            dx[35] = i5;
                            dx[36] = i4 * j1;
                            dx[37] = i3 * j2;
                            dx[38] = i2 * j3;
                            dx[39] = i1 * j4;
                            dx[40] = j5;
                            dx[41] = i4 * k1;
                            dx[42] = i3 * j1 * k1;
                            dx[43] = i2 * j2 * k1;
                            dx[44] = i1 * j3 * k1;
                            dx[45] = j4 * k1;
                            dx[46] = i3 * k2;
                            dx[47] = i2 * j1 * k2;
                            dx[48] = i1 * j2 * k2;
                            dx[49] = j3 * k2;
                            dx[50] = i2 * k3;
                            dx[51] = i1 * j1 * k3;
                            dx[52] = j2 * k3;
                            dx[53] = i1 * k4;
                            dx[54] = j1 * k4;
                            dx[55] = k5;

                            if (order > 5) {

                                double i6 = i5 * i1;
                                double j6 = j5 * j1;
                                double k6 = k5 * k1;

                                dx[56] = i6;
                                dx[57] = i5 * j1;
                                dx[58] = i4 * j2;
                                dx[59] = i3 * j3;
                                dx[60] = i2 * j4;
                                dx[61] = i1 * j5;
                                dx[62] = j6;
                                dx[63] = i5 * k1;
                                dx[64] = i4 * j1 * k1;
                                dx[65] = i3 * j2 * k1;
                                dx[66] = i2 * j3 * k1;
                                dx[67] = i1 * j4 * k1;
                                dx[68] = j5 * k1;
                                dx[69] = i4 * k2;
                                dx[70] = i3 * j1 * k2;
                                dx[71] = i2 * j2 * k2;
                                dx[72] = i1 * j3 * k2;
                                dx[73] = j4 * k2;
                                dx[74] = i3 * k3;
                                dx[75] = i2 * j1 * k3;
                                dx[76] = i1 * j2 * k3;
                                dx[77] = j3 * k3;
                                dx[78] = i2 * k4;
                                dx[79] = i1 * j1 * k4;
                                dx[80] = j2 * k4;
                                dx[81] = i1 * k5;
                                dx[82] = j1 * k5;
                                dx[83] = k6;

                                if (order > 6) {

                                    double i7 = i6 * i1;
                                    double j7 = j6 * j1;
                                    double k7 = k6 * k1;

                                    dx[84] = i7;
                                    dx[85] = i6 * j1;
                                    dx[86] = i5 * j2;
                                    dx[87] = i4 * j3;
                                    dx[88] = i3 * j4;
                                    dx[89] = i2 * j5;
                                    dx[90] = i1 * j6;
                                    dx[91] = j7;
                                    dx[92] = i6 * k1;
                                    dx[93] = i5 * j1 * k1;
                                    dx[94] = i4 * j2 * k1;
                                    dx[95] = i3 * j3 * k1;
                                    dx[96] = i2 * j4 * k1;
                                    dx[97] = i1 * j5 * k1;
                                    dx[98] = j6 * k1;
                                    dx[99] = i5 * k2;
                                    dx[100] = i4 * j1 * k2;
                                    dx[101] = i3 * j2 * k2;
                                    dx[102] = i2 * j3 * k2;
                                    dx[103] = i1 * j4 * k2;
                                    dx[104] = j5 * k2;
                                    dx[105] = i4 * k3;
                                    dx[106] = i3 * j1 * k3;
                                    dx[107] = i2 * j2 * k3;
                                    dx[108] = i1 * j3 * k3;
                                    dx[109] = j4 * k3;
                                    dx[110] = i3 * k4;
                                    dx[111] = i2 * j1 * k4;
                                    dx[112] = i1 * j2 * k4;
                                    dx[113] = j3 * k4;
                                    dx[114] = i2 * k5;
                                    dx[115] = i1 * j1 * k5;
                                    dx[116] = j2 * k5;
                                    dx[117] = i1 * k6;
                                    dx[118] = j1 * k6;
                                    dx[119] = k7;

                                    if (order > 7) {

                                        double i8 = i7 * i1;
                                        double j8 = j7 * j1;
                                        double k8 = k7 * k1;

                                        dx[120] = i8;
                                        dx[121] = i7 * j1;
                                        dx[122] = i6 * j2;
                                        dx[123] = i5 * j3;
                                        dx[124] = i4 * j4;
                                        dx[125] = i3 * j5;
                                        dx[126] = i2 * j6;
                                        dx[127] = i1 * j7;
                                        dx[128] = j8;
                                        dx[129] = i7 * k1;
                                        dx[130] = i6 * j1 * k1;
                                        dx[131] = i5 * j2 * k1;
                                        dx[132] = i4 * j3 * k1;
                                        dx[133] = i3 * j4 * k1;
                                        dx[134] = i2 * j5 * k1;
                                        dx[135] = i1 * j6 * k1;
                                        dx[136] = j7 * k1;
                                        dx[137] = i6 * k2;
                                        dx[138] = i5 * j1 * k2;
                                        dx[139] = i4 * j2 * k2;
                                        dx[140] = i3 * j3 * k2;
                                        dx[141] = i2 * j4 * k2;
                                        dx[142] = i1 * j5 * k2;
                                        dx[143] = j6 * k2;
                                        dx[144] = i5 * k3;
                                        dx[145] = i4 * j1 * k3;
                                        dx[146] = i3 * j2 * k3;
                                        dx[147] = i2 * j3 * k3;
                                        dx[148] = i1 * j4 * k3;
                                        dx[149] = j5 * k3;
                                        dx[150] = i4 * k4;
                                        dx[151] = i3 * j1 * k4;
                                        dx[152] = i2 * j2 * k4;
                                        dx[153] = i1 * j3 * k4;
                                        dx[154] = j4 * k4;
                                        dx[155] = i3 * k5;
                                        dx[156] = i2 * j1 * k5;
                                        dx[157] = i1 * j2 * k5;
                                        dx[158] = j3 * k5;
                                        dx[159] = i2 * k6;
                                        dx[160] = i1 * j1 * k6;
                                        dx[161] = j2 * k6;
                                        dx[162] = i1 * k7;
                                        dx[163] = j1 * k7;
                                        dx[164] = k8;

                                        if (order > 8) {

                                            double i9 = i8 * i1;
                                            double j9 = j8 * j1;
                                            double k9 = k8 * k1;

                                            dx[165] = i9;
                                            dx[166] = i8 * j1;
                                            dx[167] = i7 * j2;
                                            dx[168] = i6 * j3;
                                            dx[169] = i5 * j4;
                                            dx[170] = i4 * j5;
                                            dx[171] = i3 * j6;
                                            dx[172] = i2 * j7;
                                            dx[173] = i1 * j8;
                                            dx[174] = j9;
                                            dx[175] = i8 * k1;
                                            dx[176] = i7 * j1 * k1;
                                            dx[177] = i6 * j2 * k1;
                                            dx[178] = i5 * j3 * k1;
                                            dx[179] = i4 * j4 * k1;
                                            dx[180] = i3 * j5 * k1;
                                            dx[181] = i2 * j6 * k1;
                                            dx[182] = i1 * j7 * k1;
                                            dx[183] = j8 * k1;
                                            dx[184] = i7 * k2;
                                            dx[185] = i6 * j1 * k2;
                                            dx[186] = i5 * j2 * k2;
                                            dx[187] = i4 * j3 * k2;
                                            dx[188] = i3 * j4 * k2;
                                            dx[189] = i2 * j5 * k2;
                                            dx[190] = i1 * j6 * k2;
                                            dx[191] = j7 * k2;
                                            dx[192] = i6 * k3;
                                            dx[193] = i5 * j1 * k3;
                                            dx[194] = i4 * j2 * k3;
                                            dx[195] = i3 * j3 * k3;
                                            dx[196] = i2 * j4 * k3;
                                            dx[197] = i1 * j5 * k3;
                                            dx[198] = j6 * k3;
                                            dx[199] = i5 * k4;
                                            dx[200] = i4 * j1 * k4;
                                            dx[201] = i3 * j2 * k4;
                                            dx[202] = i2 * j3 * k4;
                                            dx[203] = i1 * j4 * k4;
                                            dx[204] = j5 * k4;
                                            dx[205] = i4 * k5;
                                            dx[206] = i3 * j1 * k5;
                                            dx[207] = i2 * j2 * k5;
                                            dx[208] = i1 * j3 * k5;
                                            dx[209] = j4 * k5;
                                            dx[210] = i3 * k6;
                                            dx[211] = i2 * j1 * k6;
                                            dx[212] = i1 * j2 * k6;
                                            dx[213] = j3 * k6;
                                            dx[214] = i2 * k7;
                                            dx[215] = i1 * j1 * k7;
                                            dx[216] = j2 * k7;
                                            dx[217] = i1 * k8;
                                            dx[218] = j1 * k8;
                                            dx[219] = k9;

                                            if (order > 9) {

                                                double i10 = i9 * i1;
                                                double j10 = j9 * j1;
                                                double k10 = k9 * k1;

                                                dx[220] = i10;
                                                dx[221] = i9 * j1;
                                                dx[222] = i8 * j2;
                                                dx[223] = i7 * j3;
                                                dx[224] = i6 * j4;
                                                dx[225] = i5 * j5;
                                                dx[226] = i4 * j6;
                                                dx[227] = i3 * j7;
                                                dx[228] = i2 * j8;
                                                dx[229] = i1 * j9;
                                                dx[230] = j10;
                                                dx[231] = i9 * k1;
                                                dx[232] = i8 * j1 * k1;
                                                dx[233] = i7 * j2 * k1;
                                                dx[234] = i6 * j3 * k1;
                                                dx[235] = i5 * j4 * k1;
                                                dx[236] = i4 * j5 * k1;
                                                dx[237] = i3 * j6 * k1;
                                                dx[238] = i2 * j7 * k1;
                                                dx[239] = i1 * j8 * k1;
                                                dx[240] = j9 * k1;
                                                dx[241] = i8 * k2;
                                                dx[242] = i7 * j1 * k2;
                                                dx[243] = i6 * j2 * k2;
                                                dx[244] = i5 * j3 * k2;
                                                dx[245] = i4 * j4 * k2;
                                                dx[246] = i3 * j5 * k2;
                                                dx[247] = i2 * j6 * k2;
                                                dx[248] = i1 * j7 * k2;
                                                dx[249] = j8 * k2;
                                                dx[250] = i7 * k3;
                                                dx[251] = i6 * j1 * k3;
                                                dx[252] = i5 * j2 * k3;
                                                dx[253] = i4 * j3 * k3;
                                                dx[254] = i3 * j4 * k3;
                                                dx[255] = i2 * j5 * k3;
                                                dx[256] = i1 * j6 * k3;
                                                dx[257] = j7 * k3;
                                                dx[258] = i6 * k4;
                                                dx[259] = i5 * j1 * k4;
                                                dx[260] = i4 * j2 * k4;
                                                dx[261] = i3 * j3 * k4;
                                                dx[262] = i2 * j4 * k4;
                                                dx[263] = i1 * j5 * k4;
                                                dx[264] = j6 * k4;
                                                dx[265] = i5 * k5;
                                                dx[266] = i4 * j1 * k5;
                                                dx[267] = i3 * j2 * k5;
                                                dx[268] = i2 * j3 * k5;
                                                dx[269] = i1 * j4 * k5;
                                                dx[270] = j5 * k5;
                                                dx[271] = i4 * k6;
                                                dx[272] = i3 * j1 * k6;
                                                dx[273] = i2 * j2 * k6;
                                                dx[274] = i1 * j3 * k6;
                                                dx[275] = j4 * k6;
                                                dx[276] = i3 * k7;
                                                dx[277] = i2 * j1 * k7;
                                                dx[278] = i1 * j2 * k7;
                                                dx[279] = j3 * k7;
                                                dx[280] = i2 * k8;
                                                dx[281] = i1 * j1 * k8;
                                                dx[282] = j2 * k8;
                                                dx[283] = i1 * k9;
                                                dx[284] = j1 * k9;
                                                dx[285] = k10;

                                                if (order > 10) {

                                                    double i11 = i10 * i1;
                                                    double j11 = j10 * j1;
                                                    double k11 = k10 * k1;

                                                    dx[286] = i11;
                                                    dx[287] = i10 * j1;
                                                    dx[288] = i9 * j2;
                                                    dx[289] = i8 * j3;
                                                    dx[290] = i7 * j4;
                                                    dx[291] = i6 * j5;
                                                    dx[292] = i5 * j6;
                                                    dx[293] = i4 * j7;
                                                    dx[294] = i3 * j8;
                                                    dx[295] = i2 * j9;
                                                    dx[296] = i1 * j10;
                                                    dx[297] = j11;
                                                    dx[298] = i10 * k1;
                                                    dx[299] = i9 * j1 * k1;
                                                    dx[300] = i8 * j2 * k1;
                                                    dx[301] = i7 * j3 * k1;
                                                    dx[302] = i6 * j4 * k1;
                                                    dx[303] = i5 * j5 * k1;
                                                    dx[304] = i4 * j6 * k1;
                                                    dx[305] = i3 * j7 * k1;
                                                    dx[306] = i2 * j8 * k1;
                                                    dx[307] = i1 * j9 * k1;
                                                    dx[308] = j10 * k1;
                                                    dx[309] = i9 * k2;
                                                    dx[310] = i8 * j1 * k2;
                                                    dx[311] = i7 * j2 * k2;
                                                    dx[312] = i6 * j3 * k2;
                                                    dx[313] = i5 * j4 * k2;
                                                    dx[314] = i4 * j5 * k2;
                                                    dx[315] = i3 * j6 * k2;
                                                    dx[316] = i2 * j7 * k2;
                                                    dx[317] = i1 * j8 * k2;
                                                    dx[318] = j9 * k2;
                                                    dx[319] = i8 * k3;
                                                    dx[320] = i7 * j1 * k3;
                                                    dx[321] = i6 * j2 * k3;
                                                    dx[322] = i5 * j3 * k3;
                                                    dx[323] = i4 * j4 * k3;
                                                    dx[324] = i3 * j5 * k3;
                                                    dx[325] = i2 * j6 * k3;
                                                    dx[326] = i1 * j7 * k3;
                                                    dx[327] = j8 * k3;
                                                    dx[328] = i7 * k4;
                                                    dx[329] = i6 * j1 * k4;
                                                    dx[330] = i5 * j2 * k4;
                                                    dx[331] = i4 * j3 * k4;
                                                    dx[332] = i3 * j4 * k4;
                                                    dx[333] = i2 * j5 * k4;
                                                    dx[334] = i1 * j6 * k4;
                                                    dx[335] = j7 * k4;
                                                    dx[336] = i6 * k5;
                                                    dx[337] = i5 * j1 * k5;
                                                    dx[338] = i4 * j2 * k5;
                                                    dx[339] = i3 * j3 * k5;
                                                    dx[340] = i2 * j4 * k5;
                                                    dx[341] = i1 * j5 * k5;
                                                    dx[342] = j6 * k5;
                                                    dx[343] = i5 * k6;
                                                    dx[344] = i4 * j1 * k6;
                                                    dx[345] = i3 * j2 * k6;
                                                    dx[346] = i2 * j3 * k6;
                                                    dx[347] = i1 * j4 * k6;
                                                    dx[348] = j5 * k6;
                                                    dx[349] = i4 * k7;
                                                    dx[350] = i3 * j1 * k7;
                                                    dx[351] = i2 * j2 * k7;
                                                    dx[352] = i1 * j3 * k7;
                                                    dx[353] = j4 * k7;
                                                    dx[354] = i3 * k8;
                                                    dx[355] = i2 * j1 * k8;
                                                    dx[356] = i1 * j2 * k8;
                                                    dx[357] = j3 * k8;
                                                    dx[358] = i2 * k9;
                                                    dx[359] = i1 * j1 * k9;
                                                    dx[360] = j2 * k9;
                                                    dx[361] = i1 * k10;
                                                    dx[362] = j1 * k10;
                                                    dx[363] = k11;

                                                    if (order > 11) {

                                                        double i12 = i11 * i1;
                                                        double j12 = j11 * j1;
                                                        double k12 = k11 * k1;

                                                        dx[364] = i12;
                                                        dx[365] = i11 * j1;
                                                        dx[366] = i10 * j2;
                                                        dx[367] = i9 * j3;
                                                        dx[368] = i8 * j4;
                                                        dx[369] = i7 * j5;
                                                        dx[370] = i6 * j6;
                                                        dx[371] = i5 * j7;
                                                        dx[372] = i4 * j8;
                                                        dx[373] = i3 * j9;
                                                        dx[374] = i2 * j10;
                                                        dx[375] = i1 * j11;
                                                        dx[376] = j12;
                                                        dx[377] = i11 * k1;
                                                        dx[378] = i10 * j1 * k1;
                                                        dx[379] = i9 * j2 * k1;
                                                        dx[380] = i8 * j3 * k1;
                                                        dx[381] = i7 * j4 * k1;
                                                        dx[382] = i6 * j5 * k1;
                                                        dx[383] = i5 * j6 * k1;
                                                        dx[384] = i4 * j7 * k1;
                                                        dx[385] = i3 * j8 * k1;
                                                        dx[386] = i2 * j9 * k1;
                                                        dx[387] = i1 * j10 * k1;
                                                        dx[388] = j11 * k1;
                                                        dx[389] = i10 * k2;
                                                        dx[390] = i9 * j1 * k2;
                                                        dx[391] = i8 * j2 * k2;
                                                        dx[392] = i7 * j3 * k2;
                                                        dx[393] = i6 * j4 * k2;
                                                        dx[394] = i5 * j5 * k2;
                                                        dx[395] = i4 * j6 * k2;
                                                        dx[396] = i3 * j7 * k2;
                                                        dx[397] = i2 * j8 * k2;
                                                        dx[398] = i1 * j9 * k2;
                                                        dx[399] = j10 * k2;
                                                        dx[400] = i9 * k3;
                                                        dx[401] = i8 * j1 * k3;
                                                        dx[402] = i7 * j2 * k3;
                                                        dx[403] = i6 * j3 * k3;
                                                        dx[404] = i5 * j4 * k3;
                                                        dx[405] = i4 * j5 * k3;
                                                        dx[406] = i3 * j6 * k3;
                                                        dx[407] = i2 * j7 * k3;
                                                        dx[408] = i1 * j8 * k3;
                                                        dx[409] = j9 * k3;
                                                        dx[410] = i8 * k4;
                                                        dx[411] = i7 * j1 * k4;
                                                        dx[412] = i6 * j2 * k4;
                                                        dx[413] = i5 * j3 * k4;
                                                        dx[414] = i4 * j4 * k4;
                                                        dx[415] = i3 * j5 * k4;
                                                        dx[416] = i2 * j6 * k4;
                                                        dx[417] = i1 * j7 * k4;
                                                        dx[418] = j8 * k4;
                                                        dx[419] = i7 * k5;
                                                        dx[420] = i6 * j1 * k5;
                                                        dx[421] = i5 * j2 * k5;
                                                        dx[422] = i4 * j3 * k5;
                                                        dx[423] = i3 * j4 * k5;
                                                        dx[424] = i2 * j5 * k5;
                                                        dx[425] = i1 * j6 * k5;
                                                        dx[426] = j7 * k5;
                                                        dx[427] = i6 * k6;
                                                        dx[428] = i5 * j1 * k6;
                                                        dx[429] = i4 * j2 * k6;
                                                        dx[430] = i3 * j3 * k6;
                                                        dx[431] = i2 * j4 * k6;
                                                        dx[432] = i1 * j5 * k6;
                                                        dx[433] = j6 * k6;
                                                        dx[434] = i5 * k7;
                                                        dx[435] = i4 * j1 * k7;
                                                        dx[436] = i3 * j2 * k7;
                                                        dx[437] = i2 * j3 * k7;
                                                        dx[438] = i1 * j4 * k7;
                                                        dx[439] = j5 * k7;
                                                        dx[440] = i4 * k8;
                                                        dx[441] = i3 * j1 * k8;
                                                        dx[442] = i2 * j2 * k8;
                                                        dx[443] = i1 * j3 * k8;
                                                        dx[444] = j4 * k8;
                                                        dx[445] = i3 * k9;
                                                        dx[446] = i2 * j1 * k9;
                                                        dx[447] = i1 * j2 * k9;
                                                        dx[448] = j3 * k9;
                                                        dx[449] = i2 * k10;
                                                        dx[450] = i1 * j1 * k10;
                                                        dx[451] = j2 * k10;
                                                        dx[452] = i1 * k11;
                                                        dx[453] = j1 * k11;
                                                        dx[454] = k12;
                                                    }/*End if(order>11)*/
                                                }/*End if(order>10)*/
                                            }/*End if(order>9)*/
                                        }/*End if(order>8)*/
                                    }/*End if(order>7)*/
                                }/*End if(order>6)*/
                            }/*End if(order>5)*/
                        }/*End if(order>4)*/
                    }/*End if(order>3)*/
                }/*End if(order>2)*/
            }/*End if(order>1)*/
        }/*End if(order>0)*/

        /* Find the zero,zero and one coordinates */


        double x_p = 0.0;
        double y_p = 0.0;
        double z_p = 0.0;

        double[] p0 = matrix[0];
        double[] p1 = matrix[1];
        double[] p2 = matrix[2];
        double[] p3 = dx;

        int ii;

        for (ii = 0; ii < coeffp; ii++) {
            /* x_p+=e0[ii]*dx[ii]; */
            /* y_p+=e1[ii]*dx[ii]; */
            /* z_p+=e2[ii]*dx[ii]; */

            x_p += p0[ii] * p3[ii];
            y_p += p1[ii] * p3[ii];
            z_p += p2[ii] * p3[ii];
        }


        out.x = x_p;
        out.y = y_p;
        out.z = z_p;

        return out;

    }

}
