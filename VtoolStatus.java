public class VtoolStatus{
	String status; // the description of the current job
	int percent; // % done with the current job
	String decrypted; // the decrypted text (if null, then not finished)

	public VtoolStatus(String status, int percent, String decrypted){
		this.status = status;
		this.percent = percent;
		this.decrypted = decrypted;
	}

	public VtoolStatus(VtoolStatus st){
		this.status = st.status;
		this.percent = st.percent;
		this.decrypted = st.decrypted;
	}
}

