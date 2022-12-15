package fr.packageviewer.frontend;

import java.util.List;
import java.util.Scanner;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

public class FrontendTerminal implements Frontend{

    /**
	 * Check if the String given is a number
	 * @param i the String given
	 * @return true if the String is a number
	 * @author Capelier-Marla
	 */
	 private static boolean isNumeric(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    @Override
    public SearchedPackage askUserToChoosePackage(List<SearchedPackage> packets) {
        // list all packages in reverse order
		for (int i = packets.size(); i-- > 0; ) {
			SearchedPackage searchedPacket = packets.get(i);
			System.out.printf("%s - %s/%s/%s %s%n\t%s%n",
							  i,
							  searchedPacket.getDistribution(),
							  searchedPacket.getRepo(),
							  searchedPacket.getName(),
							  searchedPacket.getVersion()==null?"":searchedPacket.getVersion(),
							  searchedPacket.getDescription());
		}

        System.out.printf("Pick a package to see in details (0-%s) : %n", packets.size()-1);
		Scanner input = new Scanner(System.in);

		// we create vars for the loop
		String packetNumberString;
		int packetNumber = -1;
		boolean notValid;
		// we ask input and check if the user input is correct
		do {
			packetNumberString = input.nextLine();
			// reset notValid to false, we set it in true only if something is wrong
			notValid = false;
			if(isNumeric(packetNumberString)) {
				packetNumber = Integer.parseInt(packetNumberString);
				if(packetNumber < 0 || packetNumber >= packets.size()) {
					// this number is too big or too small
					System.out.println("Enter a valid number");
					notValid = true;
				}
			} else {
				// this is not a number
				System.out.println("Enter a valid number");
				notValid = true;
			}
		} while(notValid);

		input.close();
        return packets.get(packetNumber);
    }

    @Override
    public void showPackageTree(Package packet) {
        // TODO Auto-generated method stub

    }

}
