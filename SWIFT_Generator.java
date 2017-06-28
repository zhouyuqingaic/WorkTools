/*
 *生成六位流水号的工具类(结合Redis)
 */
public class SWIFT_Generator{
	/*
	 * 将Ingeter转为6位数字组成的字符串
	 */
	public static String IntegerToString_6(Integer nu){
		//处理返回结果
		String result="";
		
		//个位
		result=nu%10+result;
		
		//十位
		if(nu%100>=10){
			result=nu%100/10+result;
		}else{
			result=0+result;
		}
		
		//百位
		if(nu%1000>=100){
			result=nu%1000/100+result;
		}else{
			result=0+result;
		}
	
		//千位
		if(nu%10000>=1000){
			result=nu%10000/1000+result;
		}else{
			result=0+result;
		}
		
		//万位
		if(nu%100000>=10000){
			result=nu%100000/10000+result;
		}else{
			result=0+result;
		}
		
		//十万位
		if(nu%1000000>=100000){
			result=nu%1000000/100000+result;
		}else{
			result=0+result;
		}
		
		return result;
	}
	
	
    /*
	 * 生成当日唯一的SWIFT(同步方法，避免线程安全问题)
	 */
	private static synchronized String generateSWIFT(){
		//TTcacheManager是Redis工具类
		String swift=TTcacheManager.getPersistTTCached("SWIFT");
		//当Redis中没有SWIFT的时候
		if(swift==null){
			String result_swift=IntegerToString_6(0);
			TTcacheManager.pushPersistTTCached("SWIFT", result_swift);
			return result_swift;
		}else{
			//当Redis中没有SWIFT的时候,将当前值加1并更新到Redis
			Integer result_swift_Integer=Integer.parseInt(swift)+1;
			String result_swift=IntegerToString_6(result_swift_Integer);
			TTcacheManager.pushPersistTTCached("SWIFT", result_swift);
			return result_swift;
		}
		
	}
	
	/*
	 * 测试同时多凭证生成
	 */
	public void testSWIFTGenerator(){
		//同时访问的用户列表
        List<Thread> userList=new ArrayList<Thread>();
    
        //构造20个同时访问的用户
        for(int i=0;i<20;i++){
        	final String username="user"+i;
        	
        	Thread newUser=new Thread(new Runnable() {
    			
        		//模拟生成卡券的行为，获取SWIFT流水号
    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				System.out.println(username+"----"+generateSWIFT());
    			}
    		});
        	
        	userList.add(newUser);
        }
        
        //开始访问
        for(Thread userThread:userList)
        	userThread.start();
	}
	
	//测试类
	public static void main(String[] args) throws Exception {
		//TTcacheManager.pushPersistTTCached(key, value);
		//login();
		
        new SWIFT_Generator().testSWIFTGenerator();
	}
	
}