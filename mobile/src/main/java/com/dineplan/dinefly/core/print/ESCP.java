package com.dineplan.dinefly.core.print;

import java.util.Arrays;


public class ESCP
{

    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte DLE = 16;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte CR = 13;
    public static final byte FF = 12;
    public static final byte CAN = 24;
    public static final byte SI = 15;
    public static final byte DC2 = 18;

    final static String TAG_PRN_H1 = "prn_h1";
    final static String TAG_PRN_H1_OFF = "prn_h1_off";
    final static String TAG_PRN_H2 = "prn_h2";
    final static String TAG_PRN_H2_OFF = "prn_h2_off";
    final static String TAG_PRN_H3 = "prn_h3";
    final static String TAG_PRN_H3_OFF = "prn_h3_off";
    final static String TAG_PRN_H4 = "prn_h4";
    final static String TAG_PRN_H4_OFF = "prn_h4_off";
    final static String TAG_PRN_SMALL = "prn_small";
    final static String TAG_PRN_SMALL_OFF = "prn_small_off";
    final static String TAG_PRN_ALIGN_LEFT = "prn_left";
    final static String TAG_PRN_ALIGN_RIGHT = "prn_right";
    final static String TAG_PRN_ALIGN_CENTER = "prn_center";
    final static String TAG_PRN_TAB = "prn_tab";
    final static String TAG_PRN_HR = "prn_hr";
    final static String TAG_PRN_HR_DOUBLE = "prn_hr2";


    public static String generateEndsAlignedString(String name, String value, int charsPerLine)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(pad(value, ' ', charsPerLine - name.length() - 1, false));
        return builder.toString();
    }

    public static String pad(String source, char fill, int length, boolean right)
    {
        if (source.length() > length)
        {
            return source;
        }
        char[] out = new char[length];
        if (right)
        {
            System.arraycopy(source.toCharArray(), 0, out, 0, source.length());
            Arrays.fill(out, source.length(), length, fill);
        } else
        {
            int sourceOffset = length - source.length();
            System.arraycopy(source.toCharArray(), 0, out, sourceOffset, source.length());
            Arrays.fill(out, 0, sourceOffset, fill);
        }
        return new String(out);
    }

    private static String fillWith(final String symbos, final int count)
    {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++)
        {
            builder.append(symbos);
        }

        return builder.toString();
    }

    /**
     * CodePage table
     */
    public static class CodePage
    {

        public static final byte PC437 = 0;
        public static final byte KATAKANA = 1;
        public static final byte PC850 = 2;
        public static final byte PC860 = 3;
        public static final byte PC863 = 4;
        public static final byte PC865 = 5;
        public static final byte WPC1252 = 16;
        public static final byte PC866 = 17;
        public static final byte PC852 = 18;
        public static final byte PC858 = 19;
    }

    /**
     * BarCode table
     */
    public static class BarCode
    {

        public static final byte UPC_A = 0;
        public static final byte UPC_E = 1;
        public static final byte EAN13 = 2;
        public static final byte EAN8 = 3;
        public static final byte CODE39 = 4;
        public static final byte ITF = 5;
        public static final byte NW7 = 6;
        //public static final byte CODE93      = 72;
        // public static final byte CODE128     = 73;
    }

    /**
     * Print and line feed
     * LF
     *
     * @return bytes for this command
     */
    public static byte[] print_linefeed()
    {
        byte[] result = new byte[1];
        result[0] = LF;
        return result;
    }

    /**
     * Turn underline mode on, set at 1-dot width
     * ESC - n
     *
     * @return bytes for this command
     */
    public static byte[] underline_1dot_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    /**
     * Turn underline mode on, set at 2-dot width
     * ESC - n
     *
     * @return bytes for this command
     */
    public static byte[] underline_2dot_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    /**
     * Turn underline mode off
     * ESC - n
     *
     * @return bytes for this command
     */
    public static byte[] underline_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }


    /**
     * Initialize printer
     * Clears the data in the print buffer and resets the printer modes to the modes that were
     * in effect when the power was turned on.
     * ESC @
     *
     * @return bytes for this command
     */
    public static byte[] init_printer()
    {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        return result;
    }

    /**
     * Turn emphasized mode on
     * ESC E n
     *
     * @return bytes for this command
     */
    public static byte[] emphasized_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * Turn emphasized mode off
     * ESC E n
     *
     * @return bytes for this command
     */
    public static byte[] emphasized_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    public static byte[] condensed_on()
    {
        byte[] result = new byte[1];
        result[0] = SI;
        return result;
    }

    public static byte[] condensed_off()
    {
        byte[] result = new byte[1];
        result[0] = DC2;
        return result;
    }

    /**
     * double_strike_on
     * ESC G n
     *
     * @return bytes for this command
     */
    public static byte[] double_strike_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 71;
        result[2] = 0xF;
        return result;
    }

    /**
     * double_strike_off
     * ESC G n
     *
     * @return bytes for this command
     */
    public static byte[] double_strike_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 71;
        result[2] = 0xF;
        return result;
    }

    /**
     * Select Font A
     * ESC M n
     *
     * @return bytes for this command
     */
    public static byte[] select_fontA()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 0;
        return result;
    }

    /**
     * Select Font B
     * ESC M n
     *
     * @return bytes for this command
     */
    public static byte[] select_fontB()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 1;
        return result;
    }

    /**
     * Select Font C ( some printers don't have font C )
     * ESC M n
     *
     * @return bytes for this command
     */
    public static byte[] select_fontC()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 2;
        return result;
    }

    /**
     * double height width mode on Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static byte[] double_height_width_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 56;
        return result;
    }

    /**
     * double height width mode off Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static byte[] double_height_width_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 0;
        return result;
    }

    /**
     * Select double height mode Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static byte[] double_height_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 16;
        return result;
    }

    /**
     * disable double height mode, select Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static byte[] double_height_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 0;
        return result;
    }

    /**
     * justification_left
     * ESC a n
     *
     * @return bytes for this command
     */
    public static byte[] justification_left()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }

    /**
     * justification_center
     * ESC a n
     *
     * @return bytes for this command
     */
    public static byte[] justification_center()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    /**
     * justification_right
     * ESC a n
     *
     * @return bytes for this command
     */
    public static byte[] justification_right()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }

    /**
     * Print and feed n lines
     * Prints the data in the print buffer and feeds n lines
     * ESC d n
     *
     * @param n lines
     * @return bytes for this command
     */
    public static byte[] print_and_feed_lines(byte n)
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 100;
        result[2] = n;
        return result;
    }

    /**
     * Print and reverse feed n lines
     * Prints the data in the print buffer and feeds n lines in the reserve direction
     * ESC e n
     *
     * @param n lines
     * @return bytes for this command
     */
    public static byte[] print_and_reverse_feed_lines(byte n)
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 101;
        result[2] = n;
        return result;
    }

    /**
     * Drawer Kick
     * Drawer kick-out connector pin 2
     * ESC p m t1 t2
     *
     * @return bytes for this command
     */
    public static byte[] drawer_kick()
    {
        byte[] result = new byte[5];
        result[0] = ESC;
        result[1] = 112;
        result[2] = 0;
        result[3] = 60;
        result[4] = 120;
        return result;
    }

    /**
     * Select printing color1
     * ESC r n
     *
     * @return bytes for this command
     */
    public static byte[] select_color1()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 114;
        result[2] = 0;
        return result;
    }

    /**
     * Select printing color2
     * ESC r n
     *
     * @return bytes for this command
     */
    public static byte[] select_color2()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 114;
        result[2] = 1;
        return result;
    }

    /**
     * Select character code table
     * ESC t n
     *
     * @param cp example:CodePage.WPC1252
     * @return bytes for this command
     */
    public static byte[] select_code_tab(byte cp)
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 116;
        result[2] = cp;
        return result;
    }

    /**
     * white printing mode on
     * Turn white/black reverse printing mode on
     * GS B n
     *
     * @return bytes for this command
     */
    public static byte[] white_printing_on()
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = (byte) 128;
        return result;
    }

    /**
     * white printing mode off
     * Turn white/black reverse printing mode off
     * GS B n
     *
     * @return bytes for this command
     */
    public static byte[] white_printing_off()
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = 0;
        return result;
    }

    /**
     * feed paper and cut
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a full cut ( cuts the paper completely )
     *
     * @return bytes for this command
     */
    public static byte[] feedpapercut()
    {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        return result;
    }

    /**
     * feed paper and cut partial
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a partial cut ( one point left uncut )
     *
     * @return bytes for this command
     */
    public static byte[] feedpapercut_partial()
    {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        return result;
    }

    /**
     * select bar code height
     * Select the height of the bar code as n dots
     * default dots = 162
     *
     * @param dots ( heigth of the bar code )
     * @return bytes for this command
     */
    public static byte[] barcode_height(byte dots)
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 104;
        result[2] = dots;
        return result;
    }

    /**
     * select font hri
     * Selects a font for the Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     *
     * @param n Font
     *          0, 48 Font A
     *          1, 49 Font B
     * @return bytes for this command
     */
    public static byte[] select_font_hri(byte n)
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 102;
        result[2] = n;
        return result;
    }

    /**
     * select position_hri
     * Selects the print position of Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     *
     * @param n Print position
     *          0, 48 Not printed
     *          1, 49 Above the barcode
     *          2, 50 Below the barcode
     *          3, 51 Both above and below the barcode
     * @return bytes for this command
     */
    public static byte[] select_position_hri(byte n)
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 72;
        result[2] = n;
        return result;
    }

    /**
     * print bar code
     *
     * @param barcode_typ(Barcode.CODE39,Barcode.EAN8,...)
     * @param barcode2print
     * @return bytes for this command
     */
    public static byte[] print_bar_code(byte barcode_typ, String barcode2print)
    {
        byte[] barcodebytes = barcode2print.getBytes();
        byte[] result = new byte[3 + barcodebytes.length + 1];
        result[0] = GS;
        result[1] = 107;
        result[2] = barcode_typ;
        int idx = 3;

        for (int i = 0; i < barcodebytes.length; i++)
        {
            result[idx] = barcodebytes[i];
            idx++;
        }
        result[idx] = 0;

        return result;
    }


    /**
     * Set horizontal tab positions
     *
     * @param col ( coulumn )
     * @return bytes for this command
     */
    public static byte[] set_HT_position(byte col)
    {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }
}
