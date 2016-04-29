package app.tea.functions;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CSVReadWrite {

	private final static String DIR = "/TEA";
	private FileWriter writer;
	private BufferedReader reader = null;
	private File saveFile;
	private SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.TAIWAN);
	private String mFileName = "";
	private String mFilePath = "";
	
	public CSVReadWrite(){
		makeDocument();
	}
	
	public String getSaveFileName(){
		return mFileName;
	}
	
	public String getSaveFileDir(){
		return mFilePath;
	}
	
	public void setSaveFileName(String filename) {
		try {
			mFileName = filename;
			mFilePath = DIR;
			saveFile = new File(getSDcardDir().getPath() + DIR , filename);
			writer = new FileWriter(saveFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean setReadFileName(String filename) throws IOException {
		try {
			reader = new BufferedReader(new FileReader(getSDcardDir().getPath() + DIR +"/"+ filename));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(reader == null)
			return false;
		else 
			return true;
	}

	public void closeSaveFile() {
		try{
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeReader() {
		try{
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToCSV(float[] ecg) throws IOException {
		
		try{
			StringBuilder sb = new StringBuilder();
			sb.append(sdfDateTime.format(new Date()) + ",");
			for(int i=0 ; i < ecg.length ; i++){

				sb.append(Float.toString(ecg[i])+",");
				
			}
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveStringToCSV(String ecg) throws IOException {
		
		try{
			StringBuilder sb = new StringBuilder();
			sb.append(sdfDateTime.format(new Date()) + ",");
			
			sb.append(ecg+",");
			
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToCSVwithIrrIndex(float[] data0 , float[] data1) throws IOException {
		
		try{		
			for(int i=0 ; i < data1.length ; i++){
				StringBuilder sb = new StringBuilder();
				sb.append(Float.toString(data0[i])+",");
				sb.append(Float.toString(data1[i])+",");
				sb.append("\n");
				writer.write(sb.toString());
				writer.flush();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToCSVwithMultiple(float[] data) throws IOException {
		
		try{		
			StringBuilder sb = new StringBuilder();
			sb.append(sdfDateTime.format(new Date()) + ",");
			for(int i=0 ; i < data.length ; i++){
				
				sb.append(Float.toString(data[i])+",");
			}
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToCSVwithMultiple(double[] data) throws IOException {
		
		try{		
			StringBuilder sb = new StringBuilder();
			sb.append(sdfDateTime.format(new Date()) + ",");
			for(int i=0 ; i < data.length ; i++){
				
				sb.append(Double.toString(data[i])+",");
			}
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCSVHeader(ArrayList<String> header) throws IOException{
		try{
			StringBuilder sb = new StringBuilder();
			for(int i=0 ; i < header.size() ; i++){

				sb.append(header.get(i)+",");
			}
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveToCSVwithMultiple(ArrayList<String> data) throws IOException {

		try{
			StringBuilder sb = new StringBuilder();
			for(int i=0 ; i < data.size() ; i++){

				sb.append(data.get(i)+",");
			}
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToCSVwithMultiple(int[] data) throws IOException {
		
		try{		
			StringBuilder sb = new StringBuilder();
			sb.append(sdfDateTime.format(new Date()) + ",");
			for(int i=0 ; i < data.length ; i++){
				
				sb.append(Integer.toString(data[i])+",");
			}
			sb.append("\n");
			writer.write(sb.toString());
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToCSVsingle(double data) throws IOException {
		
		try{
			StringBuilder sb = new StringBuilder();
			sb.append(sdfDateTime.format(new Date()) + ",");
			sb.append(Double.toString(data)+",");
			sb.append("\n");
			
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Float> readFromCSV() throws IOException {
		String line="";
		ArrayList<Float> value = new ArrayList<Float>();
		
		while((line = reader.readLine())  != null){
			String[] buffer = line.split(",");
			value.add(Float.parseFloat(buffer[1]));
		}
		return value;
	}

	public ArrayList<Float> readFromCSV(int colume) throws IOException {
		String line="";
		ArrayList<Float> value = new ArrayList<Float>();
		
		while((line = reader.readLine())  != null){
			String[] buffer = line.split(",");
			value.add(Float.parseFloat(buffer[colume]));
		}
		return value;
	}

	public ArrayList<Double> readFromCSVDouble(int colume) throws IOException {
		String line="";
		ArrayList<Double> value = new ArrayList<Double>();
		
		while((line = reader.readLine())  != null){
			String[] buffer = line.split(",");
			value.add(Double.parseDouble(buffer[colume]));
		}
		return value;
	}
	
	public ArrayList<Float> readFromCSV_Column0() throws IOException {
		String line="";
		ArrayList<Float> value = new ArrayList<Float>();
		while((line = reader.readLine())  != null){
			String[] buffer = line.split(",");
			value.add(Float.parseFloat(buffer[0]));
		}
		return value;
	}
	
	//create of CSV header
	public void writeCSVHeader(String header0 ,String header1 ,String header2) throws IOException {
		String line = String.format("%s,%s,%s\n", header0, header1, header2);
		writer.write(line);
	}

	public void makeDocument(){
		File dir = Environment.getExternalStorageDirectory();
		String path = dir.getPath() + DIR;
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
	}
	
	public File getSDcardDir(){
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        File sdCardDir = null;
        if(sdCardExist){
        	sdCardDir = Environment.getExternalStorageDirectory();
        }
        return sdCardDir;
	}

	
}
