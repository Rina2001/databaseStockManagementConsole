package animations;

public class Animations extends Thread{
	
	String msg;
	int speed;
	public Animations(String msg, int speed) {
		this.msg=msg;
		this.speed=speed;
		start();
	}
	
	public void showAnimations(){
		
		char[] c=msg.toCharArray();
		for(int i=0;i<c.length;i++){
			System.out.print(c[i]);
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
			this.showAnimations();
		}
	}
	

}
