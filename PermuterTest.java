public class PermuterTest{
	public static void main(String[] args){
		final int N = 6;
		Permuter perm = new Permuter(N);
		char[] p;
		while((p = perm.nextPerm()) != null){
			for(int i = 0; i < N; ++i){
				System.out.print(p[i]);
			}
			System.out.println();
		}

		System.out.println(perm.getCounter() + " permutations");
	}
}

