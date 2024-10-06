package lc.mine.skywars.utils;

public class IntegerUtil {
    public static final int X_BITS = 26;
    public static final int Y_BITS = 9;
    public static final int Z_BITS = 26;

    /*
     * Range values:
     * X: -33554432 -> 33554431
     * Y: -256 -> 255
     * Z: -33554432 -> 33554431
     */

    public static boolean outOfBounds(int x, int y, int z) {
        if (
            IntegerUtil.validateIntWithoutException(x, IntegerUtil.X_BITS) == -1 ||
            IntegerUtil.validateIntWithoutException(y, IntegerUtil.Y_BITS) == -1 ||
            IntegerUtil.validateIntWithoutException(z, IntegerUtil.Z_BITS) == -1
        ) {
            return true;
        }
        return false;
    }

    public static int calculateRows(int amountItems) {
        if (amountItems <= 9) {
            return 9; 
        }
        if (amountItems % 9 == 0) {
            return amountItems;    
        }
        return 9 * (amountItems / 9 + 1);
    }


    public static int parsePositive(final String text, final int defaultReturnValue) {
        if (text == null) {
            return defaultReturnValue; 
        }
        
        final int length = text.length();
        if (length == 1) {
            final int value = text.charAt(0) - '0';
            return (value > 9 || value < 0) ? defaultReturnValue : value;
        }

        int result = 0;
        for (int i = 0; i < length; i++) {
            int value = text.charAt(i) - '0';
            if (value > 9 || value < 0) {
                return defaultReturnValue; 
            }
            result = (result + value) * 10;
        } 
        return result / 10;
    }

    // Compacta x, y, z en un solo long
    public static long compress(int x, int y, int z, boolean validate) {
        // Validar los rangos de los valores
        if (validate) {
            validateInt(x, X_BITS);
            validateInt(y, Y_BITS);
            validateInt(z, Z_BITS);   
        }

        // Desplazamiento de bits para colocar los valores en su lugar adecuado
        long compressed = 0;
            
        // Manejo del bit de signo para x
        compressed |= ((long) (x & ((1 << X_BITS) - 1))) << (Y_BITS + Z_BITS);
            
        // Manejo del bit de signo para y
        compressed |= ((long) (y & ((1 << Y_BITS) - 1))) << Z_BITS;
            
        // Manejo del bit de signo para z
        compressed |= (long) (z & ((1 << Z_BITS) - 1));
    
        return compressed;
    }
    
    // Descomprime un long en x, y, z
    public static int[] decompress(long compressed) {
        int x = (int) ((compressed >> (Y_BITS + Z_BITS)) & ((1L << X_BITS) - 1));
        x = applySignExtension(x, X_BITS);
            
        int y = (int) ((compressed >> Z_BITS) & ((1L << Y_BITS) - 1));
        y = applySignExtension(y, Y_BITS);
            
        int z = (int) (compressed & ((1L << Z_BITS) - 1));
        z = applySignExtension(z, Z_BITS);
    
        return new int[]{x, y, z};
    }
    
    // Aplica la extensión de signo para un número dado el número de bits
    private static int applySignExtension(int value, int bits) {
    int mask = 1 << (bits - 1);
        if ((value & mask) != 0) {
            value |= ~((1 << bits) - 1);
        }
        return value;
    }
    
    // Valida que el entero se ajuste al número de bits especificado
    public static void validateInt(int value, int bits) {
        int maxValue = (1 << (bits - 1)) - 1;
        int minValue = -(1 << (bits - 1));
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Value out of range for " + bits + " bits: " + value);
        }
    }

    public static int validateIntWithoutException(int value, int bits) {
        int maxValue = (1 << (bits - 1)) - 1;
        int minValue = -(1 << (bits - 1));
        if (value < minValue || value > maxValue) {
            return -1;
        }
        return 0;
    }
}