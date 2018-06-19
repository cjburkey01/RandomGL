package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import java.nio.ByteBuffer;

public class Attribute implements Comparable<Attribute> {
    
    public final int location;
    public final int type;
    public final int count;
    
    public Attribute(int location, int type, int count) {
        this.location = location;
        this.type = type;
        this.count = count;
    }
    
    public int getSize() {
        return getSize(type, count);
    }
    
    public boolean addToBuffer(ByteBuffer buffer, Object[] data) throws Exception {
        if (type == GL_BYTE || type == GL_UNSIGNED_BYTE) {
            for (Object dat : data) {
                buffer.put((byte) dat);
            }
            return true;
        }
        if (type == GL_SHORT || type == GL_UNSIGNED_SHORT) {
            for (Object dat : data) {
                buffer.putShort((short) dat);
            }
            return true;
        }
        if (type == GL_INT || type == GL_UNSIGNED_INT) {
            if (count == 1) {
                for (Object dat : data) {
                    buffer.putInt((int) dat);
                }
                return true;
            }
            if (count == 2) {
                for (Object dat : data) {
                    int[] datt = (int[]) dat;
                    buffer.putInt(datt[0]);
                    buffer.putInt(datt[1]);
                }
                return true;
            }
            if (count == 3) {
                for (Object dat : data) {
                    int[] datt = (int[]) dat;
                    buffer.putInt(datt[0]);
                    buffer.putInt(datt[1]);
                    buffer.putInt(datt[2]);
                }
                return true;
            }
            if (count == 4) {
                for (Object dat : data) {
                    int[] datt = (int[]) dat;
                    buffer.putInt(datt[0]);
                    buffer.putInt(datt[1]);
                    buffer.putInt(datt[2]);
                    buffer.putInt(datt[3]);
                }
                return true;
            }
        }
        if (type == GL_FLOAT) {
            if (count == 1) {
                for (Object dat : data) {
                    buffer.putFloat((float) dat);
                }
                return true;
            }
            if (count == 2) {
                for (Object dat : data) {
                    float[] datt = (float[]) dat;
                    buffer.putFloat(datt[0]);
                    buffer.putFloat(datt[1]);
                }
                return true;
            }
            if (count == 3) {
                for (Object dat : data) {
                    float[] datt = (float[]) dat;
                    buffer.putFloat(datt[0]);
                    buffer.putFloat(datt[1]);
                    buffer.putFloat(datt[2]);
                }
                return true;
            }
            if (count == 4) {
                for (Object dat : data) {
                    float[] datt = (float[]) dat;
                    buffer.putFloat(datt[0]);
                    buffer.putFloat(datt[1]);
                    buffer.putFloat(datt[2]);
                    buffer.putFloat(datt[3]);
                }
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + location;
        result = prime * result + type;
        return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Attribute other = (Attribute) obj;
        if (count != other.count) {
            return false;
        }
        return true;
    }
    
    public int compareTo(Attribute o) {
        return Integer.compare(location, o.location);
    }
    
    public static int getSize(int type, int count) {
        if (type == GL_BYTE || type == GL_UNSIGNED_BYTE) {
            return 1;
        }
        if (type == GL_SHORT || type == GL_UNSIGNED_SHORT) {
            return 2;
        }
        if (type == GL_INT || type == GL_UNSIGNED_INT) {
            if (count == 1) {
                return 4;
            }
            if (count == 2) {
                return 8;
            }
            if (count == 3) {
                return 12;
            }
            if (count == 4) {
                return 16;
            }
        }
        if (type == GL_FLOAT) {
            if (count == 1) {
                return 4;
            }
            if (count == 2) {
                return 8;
            }
            if (count == 3) {
                return 12;
            }
            if (count == 4) {
                return 16;
            }
        }
        return 0;
    }
    
}