// America Rangel, Collaboration w/ Megan Taketa

public class Collaborative1 {
    public static void main(String[] args) {
        Candidate[] candidates = {
                new Candidate("Jack Johnson", "Tastycrat"),
                new Candidate("Jane Johnson", "Tastycrat"),
                new Candidate("John Jackson", "Fingerlican"),
                new Candidate("Jenny Jackson", "Fingerlican"),
                new Candidate("Hermes", "Brain Slug"),
                new Candidate("Fry", "Brain Slug")};

        System.out.println("The candidates are:");
        for (int i = 0; i < candidates.length; i++)
            System.out.println(candidates[i]);

        String[] candidateNames = new String[candidates.length]; //creating empty array with 6 candidates
        for (int i = 0; i < candidates.length; i++)
            candidateNames[i] = candidates[i].getName();  //storing the names of the candidates in a string array

        String[] parties = {"Tastycrat", "Fingerlican", "Brain Slug"}; //creates string array but with actual parties in it

        Citizen[] citizens = new Citizen[1000];
        for (int i = 0; i < citizens.length; i++)
            citizens[i] = new Citizen((int) (Math.random() * 111),  //creating new citizen object
                    randomString(parties),
                    shuffle(candidateNames.clone())); //copies candidate names and shuffles for a random ranking of candidates

        pluralityElection(candidates, citizens); //calling each method to simulate each type of election with the candidates and citizens
        instantRunoffElection(candidates, citizens);
    }

    static void pluralityElection(Candidate[] candidates, Citizen[] citizens) {
        System.out.println("\nConducting a plurality election...");
        // Collect the votes
        int[] votes = new int[candidates.length];
        for (int citNum = 0; citNum < citizens.length; citNum++) { //goes through all citizens
            Citizen cit = citizens[citNum]; //get current citizen
            if (cit.getAge() >= 18) { // if older than 18, they can vote
                String firstChoice = cit.getPreferences()[0]; //gets first choice
                for (int candNum = 0; candNum < candidates.length; candNum++)  //finds the array number for that first preference of candidate
                    if (firstChoice.equals(candidates[candNum].getName())) //checks if first choice preference is equal to the candidate number and if so gets the name of that candidate
                        votes[candNum]++; //adds vote to the first choice
            }
        }
        // Print the results
        System.out.println("... and here are the totals:");
        for (int i = 0; i < candidates.length; i++)
            System.out.println(candidates[i] + ", Votes: " + votes[i]);
        int maxVotes = 0;
        for (int i = 0; i < candidates.length; i++)
            if (votes[i] > maxVotes) maxVotes = votes[i];
        int numWinners = 0;
        for (int i = 0; i < candidates.length; i++)
            if (votes[i] == maxVotes) numWinners++;
        if (numWinners > 1) System.out.println("\nSo it's a tie! The winners are:");
        else System.out.println("\nSo the winner is:");
        for (int i = 0; i < candidates.length; i++)
            if (votes[i] == maxVotes) System.out.println(candidates[i]);
    }

    static void instantRunoffElection(Candidate[] candidates, Citizen[] citizens) {
        System.out.println("\nConducting an instant runoff election...");
        int numVoters = 0;
        for (int i = 0; i < citizens.length; i++) {
            if (citizens[i].getAge() >= 18)
                numVoters++; // will later help us determine how many votes needed to win 50% or more of the votes
        }

        // Collect the votes
        String[] eliminatedCandidates = new String[candidates.length]; // create an array to store the names of eliminated candidates
        for (int i = 0; i < candidates.length; i++) eliminatedCandidates[i] = "";  //going through empty array for eliminated candidates, setting each element to be an empty string
        int round = 0; // number of rounds in election (there could only be one if one candidate wins >= 50% of votes)
        while (round < candidates.length) {
            int[] votes = new int[6]; // array that holds
            // Get citizens votes
            for (int citNum = 0; citNum < citizens.length; citNum++) { // goes through all citizens
                Citizen cit = citizens[citNum];
                if (cit.getAge() >= 18) { // for current citizen, if > 18 years of age, get vote
                    int prefIndex = 0; // call string in array (goes through eliminated candidates
                    while (stringInArray(cit.getPreferences()[prefIndex], eliminatedCandidates)) {
                        prefIndex++;
                    }
                    String pref = cit.getPreferences()[prefIndex];

                    for (int candNum = 0; candNum < candidates.length; candNum++) {
                        if (pref.equals(candidates[candNum].getName())) {
                            votes[candNum]++;
                        }
                    }
                }
            }

            System.out.println("\nRound " + (round+1));
            for (int candNum = 0; candNum < candidates.length; candNum++) {
                String candidateName = candidates[candNum].getName();
                if (stringInArray(candidateName, eliminatedCandidates) == false) {
                    System.out.println(candidateName + " got " + votes[candNum] + " votes.");
                }
            }

            int winningVotes = numVoters / 2;
            for (int candNum = 0; candNum < votes.length; candNum++) {
                if (votes[candNum] > winningVotes) {
                    String winnerName = candidates[candNum].getName();
                    System.out.println(winnerName + " won the election!");
                    return;
                }
            }

            int lowestVotes = numVoters;
            int lowestVoteCandNum = 0;
            for (int candNum = 0; candNum < candidates.length; candNum++) {
                String candName = candidates[candNum].getName();
                if (votes[candNum] < lowestVotes && (!stringInArray(candName, eliminatedCandidates))) {
                    lowestVotes = votes[candNum];
                    lowestVoteCandNum = candNum;
                }
            }

            String eliminatedCandidateName = candidates[lowestVoteCandNum].getName();
            System.out.println(eliminatedCandidateName + " was eliminated.");
            eliminatedCandidates[round] = eliminatedCandidateName;
            round++;
        }
    }

    static boolean stringInArray(String s, String[] a) {
        for (int i = 0; i < a.length; i++)
            if (a[i].equals(s)) return true;
        return false;

        //checks to see if candidate preference is in the eliminated array
        //if true, move on to next preference
    }

    static String randomString(String[] options) {
        return options[(int) (options.length * Math.random())];
    }

    static String[] shuffle(String[] a) {
        for (int i = a.length - 1; i > 0; i--)
            swap(a, i, (int) (Math.random() * (i + 1)));
        return a;
    }

    static void swap(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}

class Candidate {
    private String name;
    private String party;

    public Candidate(String n, String p) {
        name = n;
        party = p;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String toString() {
        return "Name: " + name + ", Party: " + party;
    }
}

class Citizen {
    private int age;
    private String party;
    private String[] preferences;

    public Citizen(int a, String p, String[] prefs) {
        age = a;
        party = p;
        preferences = prefs;
    }

    public int getAge() {
        return age;
    }

    public String getParty() {
        return party;
    }

    public String[] getPreferences() {
        return preferences;
    }

    public String toString() {
        return "Age: " + age + ", Party: " + party + ", Prefs: "
                + stringArrayString(preferences);
    }

    static String stringArrayString(String[] a) {
        String result = "{";
        for (int i = 0; i < a.length; i++) {
            result = result + a[i];
            if (i < a.length - 1) result = result + ", ";
            else result = result + "}";
        }
        return result;
    }
}
