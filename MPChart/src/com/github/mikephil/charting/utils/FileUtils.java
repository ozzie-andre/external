// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

//import java.util.Iterator;
//import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import android.content.res.AssetManager;
import java.io.IOException;
import android.util.Log;
import com.github.mikephil.charting.data.BarEntry;
//import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import android.os.Environment;
import com.github.mikephil.charting.data.Entry;
import java.util.List;

public class FileUtils
{
    //private static final String LOG = "MPChart-FileUtils";
	// reserved for future use...
    
    public static List<Entry> loadEntriesFromFile(final String path) {
        final File sdcard = Environment.getExternalStorageDirectory();
        final File file = new File(sdcard, path);
        final List<Entry> entries = new ArrayList<Entry>();
        try {
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                final String[] split = line.split("#");
                if (split.length <= 2) {
                    entries.add(new Entry(Float.parseFloat(split[0]), Integer.parseInt(split[1])));
                }
                else {
                    final float[] vals = new float[split.length - 1];
                    for (int i = 0; i < vals.length; ++i) {
                        vals[i] = Float.parseFloat(split[i]);
                    }
                    entries.add(new BarEntry(vals, Integer.parseInt(split[split.length - 1])));
                }
            }
            if(br!=null) br.close(); // close this... 
        }
        catch (IOException e) {
            Log.e("MPChart-FileUtils", e.toString());
        }
        return entries;
    }
    
    public static List<Entry> loadEntriesFromAssets(final AssetManager am, final String path) {
        final List<Entry> entries = new ArrayList<Entry>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(am.open(path), "UTF-8"));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                final String[] split = line.split("#");
                if (split.length <= 2) {
                    entries.add(new Entry(Float.parseFloat(split[0]), Integer.parseInt(split[1])));
                }
                else {
                    final float[] vals = new float[split.length - 1];
                    for (int i = 0; i < vals.length; ++i) {
                        vals[i] = Float.parseFloat(split[i]);
                    }
                    entries.add(new BarEntry(vals, Integer.parseInt(split[split.length - 1])));
                }
            }
        }
        catch (IOException e) {
            Log.e("MPChart-FileUtils", e.toString());
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e2) {
                    Log.e("MPChart-FileUtils", e2.toString());
                }
                return entries;
            }
            return entries;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e2) {
                    Log.e("MPChart-FileUtils", e2.toString());
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e2) {
                Log.e("MPChart-FileUtils", e2.toString());
            }
        }
        return entries;
    }
    
    public static void saveToSdCard(final List<Entry> entries, final String path) {
        final File sdcard = Environment.getExternalStorageDirectory();
        final File saved = new File(sdcard, path);
        if (!saved.exists()) {
            try {
                saved.createNewFile();
            }
            catch (IOException e) {
                Log.e("MPChart-FileUtils", e.toString());
            }
        }
        try {
            final BufferedWriter buf = new BufferedWriter(new FileWriter(saved, true));
            for (final Entry e2 : entries) {
                buf.append((CharSequence)(String.valueOf(e2.getVal()) + "#" + e2.getXIndex()));
                buf.newLine();
            }
            buf.close();
        }
        catch (IOException e) {
            Log.e("MPChart-FileUtils", e.toString());
        }
    }
    
    public static List<BarEntry> loadBarEntriesFromAssets(final AssetManager am, final String path) {
        final List<BarEntry> entries = new ArrayList<BarEntry>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(am.open(path), "UTF-8"));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                final String[] split = line.split("#");
                entries.add(new BarEntry(Float.parseFloat(split[0]), Integer.parseInt(split[1])));
            }
        }
        catch (IOException e) {
            Log.e("MPChart-FileUtils", e.toString());
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e2) {
                    Log.e("MPChart-FileUtils", e2.toString());
                }
                return entries;
            }
            return entries;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e2) {
                    Log.e("MPChart-FileUtils", e2.toString());
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e2) {
                Log.e("MPChart-FileUtils", e2.toString());
            }
        }
        return entries;
    }
}
