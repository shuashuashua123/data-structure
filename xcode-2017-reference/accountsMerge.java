/**
	 * Accounts Merge
*Given a list accounts, each element accounts[i] is a list of strings, where the first element accounts[i][0] is a name, and the rest of the elements are emails representing emails of the account.
*
*Now, we would like to merge these accounts. Two accounts definitely belong to the same person if there is some email that is common to both accounts. Note that even if two accounts have the same name, they may belong to different people as people could have the same name. A person can have any number of accounts initially, but all of their accounts definitely have the same name.
*
*After merging the accounts, return the accounts in the following format: the first element of each account is the name, and the rest of the elements are emails in sorted order. The accounts themselves can be returned in any order.
*
*Example 1:
*Input: 
*accounts = [["John", "johnsmith@mail.com", "john00@mail.com"], ["John", "johnnybravo@mail.com"], ["John", "johnsmith@mail.com", "john_newyork@mail.com"], ["Mary", "mary@mail.com"]]
*Output: [["John", 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com'],  ["John", "johnnybravo@mail.com"], ["Mary", "mary@mail.com"]]
*Explanation: 
*The first and third John's are the same person as they have the common email "johnsmith@mail.com".
*The second John and Mary are different people as none of their email addresses are used by other accounts.
*We could return these lists in any order, for example the answer [['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'], 
*['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']] would still be accepted.
*Note:
*
*The length of accounts will be in the range [1, 1000].
*The length of accounts[i] will be in the range [1, 10].
*The length of accounts[i][j] will be in the range [1, 30].
	 */
	public List<List<String>> accountsMerge(List<List<String>> accounts) {
		Map<String, String> emailToName = new HashMap<>();
		Map<String, Integer> emailToID = new HashMap<>();
		int id = 0;
		for (List<String> account : accounts) {
			String name = "";
			for (String email : account) {
				if (name == "") {
					name = email;
					continue;
				}
				emailToName.put(email, name);
				if (!emailToID.containsKey(email)) {
					emailToID.put(email, id++);
				}
			}
		}

		UnionFind u = new UnionFind(emailToID.size());
		for (int i = 0; i < emailToID.size(); i++) {
			u.id[i] = i;
			u.size[i] = 1;
		}
		for (List<String> account : accounts) {
			for (int i = 2; i < account.size(); i++) {
				u.union(emailToID.get(account.get(i)), emailToID.get(account.get(1)));
			}
		}

		Map<Integer, List<String>> ans = new HashMap<>();
		for (String email : emailToName.keySet()) {
			int index = u.root(emailToID.get(email));
			ans.computeIfAbsent(index, x -> new ArrayList<>()).add(email);
		}
		for (List<String> component : ans.values()) {
			Collections.sort(component);
			component.add(0, emailToName.get(component.get(0)));
		}
		return new ArrayList<>(ans.values());
	}