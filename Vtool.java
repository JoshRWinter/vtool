public class Vtool{
	private Vigenere vig;
	private VtoolStatus status;

	public Vtool(String text, int lower, int upper){
		this.vig = new Vigenere(this, text, lower, upper);
		this.status = new VtoolStatus("",0,null, 0);
	}

	public String getKey(){
		if(this.vig.getState() != Thread.State.TERMINATED)
			return "";
		return this.vig.getKey();
	};

	public String[] getFoundWords(){
		if(this.vig.getState() != Thread.State.TERMINATED)
			return null;
		return this.vig.getFoundWords();
	}

	public String getCText(){
		return this.vig.getCText();
	}

	public void decrypt(){
		this.vig.start();
	}

	public synchronized VtoolStatus status(VtoolStatus newStatus){
		if(newStatus == null){ // get status
			return new VtoolStatus(this.status); // be sure to make a copy before returning
		}
		else{ // set status
			this.status.status = newStatus.status;
			this.status.percent = newStatus.percent;
			this.status.decrypted = newStatus.decrypted;
			return null;
		}
	}
}

