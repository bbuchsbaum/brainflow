// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3)
// Source File Name:   LinearSet3D.java

package brainflow.image;

import brainflow.image.iterators.XYZIterator;
import brainflow.math.Vector3f;

// Referenced classes of package brainflow.image:
//            ProbeSet, LinearSet1D, XYZIterator

public class LinearSet3D extends ProbeSet {

    class LinearProbeSet3DIterator implements XYZIterator {

        public Vector3f next() {
            Vector3f pt = new Vector3f((float) xset.getSample(idx % xlen), (float) yset.getSample((idx % planeLen) / xlen), (float) zset.getSample(idx / planeLen));
            idx++;
            return pt;
        }

        public Vector3f next(Vector3f holder) {
            holder.setX((float) xset.getSample(idx % xlen));
            holder.setY((float) yset.getSample((idx % planeLen) / xlen));
            holder.setZ((float) zset.getSample(idx / planeLen));
            idx++;
            return holder;
        }

      
        public boolean hasNext() {
            return idx < totalLen - 1;
        }

        public int getXIndex() {
            return idx % xlen;
        }

        public int getYIndex() {
            return (idx % planeLen) / xlen;
        }

        public int getZIndex() {
            return idx / planeLen;
        }

        public int getIndex() {
            return idx;
        }

        public int nextIndex() {
            int tmp = idx;
            idx++;
            return tmp;
        }

        int idx;
        int xlen;
        int ylen;
        int zlen;
        int totalLen;
        int planeLen;

        public LinearProbeSet3DIterator() {
            idx = 0;
            xlen = xset.length();
            ylen = yset.length();
            zlen = zset.length();
            planeLen = xlen * ylen;
            totalLen = xlen * ylen * zlen;
        }
    }


    public LinearSet3D(LinearSet1D _xset, LinearSet1D _yset, LinearSet1D _zset) {
        xset = _xset;
        yset = _yset;
        zset = _zset;
    }

    public double[] getSamples(int xidx, int yidx, int zidx) {
        return (new double[]{
                xset.getSample(xidx), yset.getSample(yidx), zset.getSample(zidx)
        });
    }

    public int getXLength() {
        return xset.length();
    }

    public int getYLength() {
        return yset.length();
    }

    public int getZLength() {
        return zset.length();
    }

    public double[] getXSamples() {
        return xset.getSamples();
    }

    public double[] getYSamples() {
        return yset.getSamples();
    }

    public double[] getZSamples() {
        return zset.getSamples();
    }

    public XYZIterator iterator() {
        return new LinearProbeSet3DIterator();
    }

    LinearSet1D xset;
    LinearSet1D yset;
    LinearSet1D zset;
}
