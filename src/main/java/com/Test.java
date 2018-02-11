package com;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] objectids = new String[33];
		objectids[0]="3c772f9670fe4e04b68e8a5b09bc494c";
		objectids[1]="3c772f9670fe4e04b68e8a5b09bc494c";
		objectids[2]="3c772f9670fe4e04b68e8a5b09bc494c";
		objectids[3]="3c772f9670fe4e04b68e8a5b09bc494c";
		objectids[4]="3c772f9670fe4e04b68e8a5b09bc494c";
		objectids[49]="3c772f9670fe4e04b68e8a5b09bc494c";
	}
	public String getSearchObjectids(String[] objectids,int currentNo,int pageSize){
		int totalNo=objectids.length/pageSize+1;
		StringBuffer buffer = new StringBuffer();
		if (totalNo==currentNo) {
			//最后一页  
			for (int i = totalNo*pageSize; i < objectids.length; i++) {
				String id = objectids[i];
				buffer.append(id+",");
			}
		}else {
			
		}
		return buffer.toString();  //3c772f9670fe4e04b68e8a5b09bc494c,3c772f9670fe4e04b68e8a5b09bc494c,3c772f9670fe4e04b68e8a5b09bc494c
	}
}
