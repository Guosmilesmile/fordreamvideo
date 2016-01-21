package cn.edu.xmu.ForDream.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageUtils {

	final static int FREE_SD_SPACE_NEEDED_TO_CACHE=10;	//10MB缓存空间在SD卡中存储图片
	final static int CACHE_SIZE=10;						//缓存大小10MB
	final static int MB=1024*1024*8;					//1MB=1024*8bit
	final static String TAG="Img Error";
	final static int mTimeDiff=60000;
	final static String DIR_NAME="Imgs";
	final static String WHOLESALE_CONV=".JPEG";			//图片保存后缀
	/** 
	 
	* 以最省内存的方式读取本地资源的图片 
	 
	* @param context 
	 
	* @param resId 
	 
	* @return 
	 
	*/ 	 
	public static Bitmap readBitMap(Context context, int resId){ 
	  
	BitmapFactory.Options opt = new BitmapFactory.Options(); 	 
	opt.inPreferredConfig = Bitmap.Config.RGB_565; 	 
	opt.inPurgeable = true; 
	opt.inInputShareable = true; 
	 
	//获取资源图片 
	 
	InputStream is = context.getResources().openRawResource(resId); 
	return BitmapFactory.decodeStream(is,null,opt); 
	 
	} 
	

	private static String getDirName()
	{
		return Environment.getExternalStorageDirectory().toString()+"/"+DIR_NAME;
	}
	
	public static void saveBmpToSd(Bitmap bm, String url) { 
		if (bm == null) { 
            Log.w(TAG, " trying to savenull bitmap"); 
            return; 
        } 
         //判断sdcard上的空间 
        if (FREE_SD_SPACE_NEEDED_TO_CACHE >freeSpaceOnSd()) { 
            Log.w(TAG, "Low free space onsd, do not cache"); 
            return; 
        } 
        String filename =convertUrlToFileName(url); 
        String dir =  getDirName();
        
        Log.i("FileName:","FileName:"+dir +"/" + filename);
        File file = new File(dir +"/" + filename); 
        try { 
            file.createNewFile(); 
            OutputStream outStream = new FileOutputStream(file); 
           bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
            outStream.flush(); 
            outStream.close(); 
            Log.i(TAG, "Image saved tosd"); 
        } catch (FileNotFoundException e) { 
            Log.w(TAG,"FileNotFoundException"); 
        } catch (IOException e) { 
            Log.w(TAG,"IOException"); 
        } 
	}
	private  static String convertUrlToFileName(String url) {
		String fileName=null;
		fileName=url+WHOLESALE_CONV;
	
		return fileName;
	}

	/** 
	 * 计算sdcard上的剩余空间 
	 * @return 
	 */ 
	private static int freeSpaceOnSd() { 
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath()); 
	    double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB; 
	    
	    Log.i("sdFreeMB","sdFreeMB:"+sdFreeMB);
	    return (int) sdFreeMB; 
	} 
	/** 
	 * 修改文件的最后修改时间 
	 * @param dir 
	 * @param fileName 
	 */ 
	private void updateFileTime(String dir,String fileName) { 
	    File file = new File(dir,fileName);        
	    long newModifiedTime =System.currentTimeMillis(); 
	    file.setLastModified(newModifiedTime); 
	} 
	/** 
	 *计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定 
	 * 那么删除40%最近没有被使用的文件 
	 * @param dirPath 
	 * @param filename 
	 */ 
	private void removeCache(String dirPath) { 
	    File dir = new File(dirPath); 
	    File[] files = dir.listFiles(); 
	    if (files == null) { 
	        return; 
	    } 
	    int dirSize = 0; 
	    for (int i = 0; i < files.length;i++) { 
	        if(files[i].getName().contains(WHOLESALE_CONV)) { 
	            dirSize += files[i].length(); 
	        } 
	    } 
	    if (dirSize > CACHE_SIZE * MB ||FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) { 
	        int removeFactor = (int) ((0.4 *files.length) + 1); 
	 
	        Arrays.sort(files, new FileLastModifSort()); 
	 
	        Log.i(TAG, "Clear some expiredcache files "); 
	 
	        for (int i = 0; i <removeFactor; i++) { 
	 
	            if(files[i].getName().contains(WHOLESALE_CONV)) { 
	 
	                files[i].delete();              
	 
	            } 
	 
	        } 
	 
	    } 
	 
	} 
	/** 
	 * 删除过期文件 
	 * @param dirPath 
	 * @param filename 
	 */ 
	private void removeExpiredCache(String dirPath, String filename) { 
	 
	    File file = new File(dirPath,filename); 
	 
	    if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) { 
	 
	        Log.i(TAG, "Clear some expiredcache files "); 
	 
	        file.delete(); 
	 
	    } 
	 
	} 
	/** 
	 * TODO 根据文件的最后修改时间进行排序 * 
	 */ 
	class FileLastModifSort implements Comparator<File>{ 
	    public int compare(File arg0, File arg1) { 
	        if (arg0.lastModified() >arg1.lastModified()) { 
	            return 1; 
	        } else if (arg0.lastModified() ==arg1.lastModified()) { 
	            return 0; 
	        } else { 
	            return -1; 
	        } 
	    } 
	} 
}
