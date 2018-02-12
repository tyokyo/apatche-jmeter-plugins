package elonmeter.csv.jmeter;


public class SequenceNumber {  
	private int size;
	public SequenceNumber(int size) {
		// TODO Auto-generated constructor stub
		this.size=size;
	}
	//  ①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值    
	private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {  
		public Integer initialValue() {  
			return 0;  
		}  
	};  

	public int getNextNum() {  
		int result = seqNum.get();
		if (result==size) {
			seqNum.set(0);
			result = seqNum.get();
		}
		seqNum.set(seqNum.get()+1);  
		return result; 
	}  
}  