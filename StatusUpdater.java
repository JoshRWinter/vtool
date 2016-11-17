public class StatusUpdater extends Thread{
	private GraphicalMain gm;

	public StatusUpdater(GraphicalMain parent){
		this.gm = parent;
	}

	public void run(){
		Vtool vtool = this.gm.vtool();
		vtool.decrypt();

		VtoolStatus vs;
		do{
			vs = vtool.status(null);
			this.gm.setStatus(vs.status);
			try{
				Thread.sleep(400);
			}catch(Exception e){
			}
		}while(vs.decrypted == null);

		this.gm.setText(vs.decrypted);
	}
}
