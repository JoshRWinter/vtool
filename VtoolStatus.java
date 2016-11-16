public class VtoolStatus{
	String status; // the description of the current job
	int percent; // % done with the current job
	String decrypted; // the decrypted text (if null, then not finished)
	int attempt; // the # of attempts so far

	public VtoolStatus(String status, int percent, String decrypted, int attempt){
		this.status = status;
		this.percent = percent;
		this.decrypted = decrypted;
		this.attempt = attempt;
	}

	public VtoolStatus(VtoolStatus st){
		this.status = st.status;
		this.percent = st.percent;
		this.decrypted = st.decrypted;
		this.attempt = st.attempt;
	}
}

