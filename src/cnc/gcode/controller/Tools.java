/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc.gcode.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import javax.swing.JOptionPane;

/**
 *
 * @author patrick
 */
public class Tools {
    private static final ThreadLocal<DecimalFormat> df= new ThreadLocal<DecimalFormat>(){

        @Override
        protected DecimalFormat initialValue() {
            DecimalFormatSymbols s = new DecimalFormatSymbols();
            s.setDecimalSeparator('.');
            s.setMonetaryDecimalSeparator('.');
            s.setMinusSign('-');
            return new DecimalFormat("0.0000",s); 
        }
        
    };
        
    public static Double strtod(String s) throws ParseException
    {
        return df.get().parse(s).doubleValue();
    }

    /**
     * on error returns 0.0
     * @param s
     * @return
     * @throws ParseException 
     */
    public static Double strtodsave(String s)
    {
        try {
            return strtod(s);
        } catch (ParseException ex) {
            return 0.0;
        }
    }
    
    public static String dtostr(double d)
    {
        return df.get().format(d);
    }
    
    public static String formatDuration(long secounds)
    {
        return String.format("%d:%02d:%02d", secounds/3600,(secounds%3600)/60,(secounds%60));
    }
    
    public static String convertToMultiline(String orig)
    {
        return "<html>" + orig.replaceAll("\n", "<br>");
    }    
    
    public static Double[] getValues(String[] messages,Double[] defaults, Double[] max, Double[] min)
    {
        if(messages.length!=defaults.length || messages.length!=max.length || messages.length!=min.length)
            throw new IllegalArgumentException("Length of parameter not the same!");

        Double[] values= new Double[messages.length];

        for(int i=0;i<messages.length;i++)
        {
            values[i]=defaults[i];
            while(true)
            {
                try
                {
                    values[i]=Tools.strtod(JOptionPane.showInputDialog(messages[i], Tools.dtostr(values[i])));
                }
                catch(ParseException ex)
                {
                    JOptionPane.showMessageDialog(null, ex.toString() );
                    continue;
                }
                catch(NullPointerException ex)
                {
                    return null;
                }
                if(values[i]<min[i])
                {
                    JOptionPane.showMessageDialog(null, "The value should be bigger then "+Tools.dtostr(min[i]) );
                    continue;
                }
                if(values[i]>max[i])
                {
                    JOptionPane.showMessageDialog(null, "The value should be less then "+Tools.dtostr(max[i]) );
                    continue;
                }
                
                break;
            }
        }
        return values;
    }

    public static String getJarName()
    {
        return new java.io.File(Tools.class.getProtectionDomain()
                     .getCodeSource()
                     .getLocation()
                     .getPath())
                   .getName();
    }
    
}

