package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode,
 * with fields for tag/text, first child and sibling.
 * 
 */
public class Tree {

	/**
	 * Root node
	 */
	TagNode root = null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc
	 *            Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file. The root of the tree is stored
	 * in the root field.
	 */
	public void build() {
		
		root = build2(); // use this if you remove your arguments
		
	}
	
	private TagNode build2() {
		int len;
		String cline = null;
		boolean domlines = sc.hasNextLine();
		
		// Check whether if all of the lines of html of the dom have been exhausted.
		if (domlines == true)
			cline = sc.nextLine();
		else
			return null; 
		
		len = cline.length();
		boolean x = false;
		// Check for a tag or a closing tag. If closing, do nothing. If open, set up for the following recursion.
		if (cline.charAt(0) == '<'){						// "<" Denotes the start of a tag
			cline = cline.substring(1, len - 1);
			if (cline.charAt(0) == '/')						// "/" denotes a closing tag e.g. </p>
				return null;
			else {
				x = true; 
			}
		}
		// Create the new TagNode for the tree.
		TagNode temp = new TagNode (cline, null, null);
		if(x == true)
			temp.firstChild = build2();											// Only tags have a firstChild
		temp.sibling = build2();												// Add to tree if just plain text
		return temp;
	}
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag
	 *            Old tag
	 * @param newTag
	 *            Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		replaceTag2(root, oldTag, newTag);
	}
	
	private void replaceTag2(TagNode temp, String oldTag2, String newTag2) {
		// Start at the root
		TagNode curr = temp;
		
		// Base Case
		if (curr == null)
			return;
		
		// See if the tags match up
		if (curr.tag.equals(oldTag2)) {
			curr.tag = newTag2;
		}
		
		// Inorder Traversal
		replaceTag2(temp.firstChild, oldTag2, newTag2);
		replaceTag2(temp.sibling, oldTag2, newTag2);
	}

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The
	 * boldface (b) tag appears directly under the td tag of every column of
	 * this row.
	 * 
	 * @param row
	 *            Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) { 
		TagNode curr = new TagNode(null, null, null);										// Create a TagNode to allow traversal
		TagNode temp;
		
		// See if there was a table in the html from the helper method
		curr = boldrow2(root);
		if (curr == null) {
			System.out.println("No table found");
			return;
		}
		
		// Move onto the next TagNode, hitting tr, the next row.
		curr = curr.firstChild;
		
		// Go through the rows of the table
		for(int i = 1; i < row; i++) {
			curr = curr.sibling;
		} 
		
		// Go through the columns of the table
		for (temp = curr.firstChild; temp != null; temp = temp.sibling) {
			temp.firstChild = new TagNode("b", temp.firstChild, null);
			
		}

	} 
	
	private TagNode boldrow2(TagNode curr) { 
		// Base Case
		if (curr == null)
			return null; 
		
		TagNode nodetemp = null;
		String strtemp = curr.tag;
		
		if(strtemp.equals("table")) { 
			nodetemp = curr; 
			return nodetemp;
		} 
		
		// Traverse the DOM Tree by checking the firstChild and siblings
		if(nodetemp == null) {
			nodetemp = boldrow2(curr.firstChild);
		}
		
		if(nodetemp == null) { 
			nodetemp = boldrow2(curr.sibling);
		} 
		
		
		
		return nodetemp;
	}

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em,
	 * or b, all occurrences of the tag are removed. If the tag is ol or ul,
	 * then All occurrences of such a tag are removed from the tree, and, in
	 * addition, all the li tags immediately under the removed tag are converted
	 * to p tags.
	 * 
	 * @param tag
	 *            Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		removeTag2(null, root, tag);
	}
	
	private void removeTag2(TagNode prev, TagNode curr, String tag2) {
		// Base Case
		if (curr == null)
			return;
		
		// Category 1: p, em and b tags, all occurrences of such a tag are deleted from the tree
		if(curr.tag.equals("b") || curr.tag.equals("em") || curr.tag.equals("p")) {
		if (curr.tag.equals(tag2)) {
			// See if the previous has a firstChild. (you have to account for the prev.firstChild pointer after deletion)
			if (prev.firstChild != null && prev.firstChild.tag.equals(curr.tag)) {						// After the && is to prevent if 2 tags alike are next to each other
				// If the target has a sibling
				if(curr.sibling != null) {
					//System.out.println("hey1");
					if (curr.firstChild.sibling != null) {												// This block of code ensures that if curr.firstChild has siblings, then they will also be included after the parent is removed
						TagNode temp = curr.firstChild;
						prev.sibling = temp;
						while (temp.sibling != null) {
							temp = temp.sibling;
						}
						temp.sibling = curr.sibling;
						curr.firstChild = null;
						curr.sibling = null;
					}
					else {																				// Otherwise act as if curr.firstChild is alone
						curr.firstChild.sibling = curr.sibling;
						prev.firstChild = curr.firstChild;
					}
				}
				// If the target does not have a sibling
				else {
					//System.out.println("hey2");
					prev.firstChild = curr.firstChild;							// prev.firstChild pointer points directly to the curr.firstChild. 
				}
			}
			// If the previous has a sibling. (account for prev.sibling pointer after deletion)
			else if (prev.sibling != null) {
				// If the target has a sibling
				if(curr.sibling != null) {
				if (curr.firstChild.sibling != null) {
					TagNode temp = curr.firstChild;
					prev.sibling = temp;
					while (temp.sibling != null) {
						temp = temp.sibling;
					}
					temp.sibling = curr.sibling;
					curr.firstChild = null;
					curr.sibling = null;
				}
					
				else {
					curr.firstChild.sibling = curr.sibling;						// Error
					prev.sibling = curr.firstChild;
				}
				}
				// If the target does not have a sibling
				else {
					//System.out.println("hey4");
					prev.sibling = curr.firstChild;
				}
			}
		}
		}
		
		// Category 2: ol or ul tags, all occurrences of such a tag are deleted from the tree; all li tags immediately under are converted to p tags
		else if(curr.tag.equals("ol") || curr.tag.equals("ul")) {
					
				// See if the tags match up
				if (curr.tag.equals(tag2)) {
					// If the previous has a firstChild. (account for the prev.firstChild pointer after deletion)
					if (prev.firstChild != null && prev.firstChild.tag.equals(curr.tag)) {										// After the && is to prevent if 2 tags alike are next to each other
						// If the target has a sibling
						if(curr.sibling != null) {
							if (curr.firstChild.sibling != null) {
								TagNode temp = curr.firstChild;
								while (temp.sibling != null) {
									if (temp.tag.equals("li"))
										temp.tag = "p";
									temp = temp.sibling;
								}
								
								if (temp.tag.equals("li"))
									temp.tag = "p";
								temp.sibling = curr.sibling;
								prev.firstChild = curr.firstChild;
							}
							else {
								if (curr.firstChild.tag.equals("li")) 
									curr.firstChild.tag = "p";
								curr.firstChild.sibling = curr.sibling;
								prev.firstChild = curr.firstChild;
							}
						}
						// If the target does not have a sibling
						else {
							if (curr.firstChild.sibling != null) {
								TagNode temp = curr.firstChild;
								while(temp.sibling != null) {
									if (temp.tag.equals("li"))
										temp.tag = "p";
									temp = temp.sibling;
								}
								if (temp.tag.equals("li"))
									temp.tag = "p";
								prev.firstChild = curr.firstChild;
							}
							else {
								if (curr.firstChild.tag.equals("li")) 
									curr.firstChild.tag = "p";	
								prev.firstChild = curr.firstChild;							// prev.firstChild pointer points directly to the curr.firstChild.
							}
						}
					}
					// If the previous has a sibling. (you have to account for prev.sibling pointer after deletion)
					else if (prev.sibling != null) {
						// If the target has a sibling
						if(curr.sibling != null) {
							//System.out.println("hay3");
							if (curr.firstChild.tag.equals("li"))
								curr.firstChild.tag = "p";
						
							if (curr.firstChild.sibling != null) {												// This block of code ensures that if curr.firstChild has siblings, then they will also be included after the parent is removed
								TagNode temp = curr.firstChild;
								prev.sibling = temp;
								while (temp.sibling != null) {
									if (temp.tag.equals("li"))
										temp.tag = "p";
									temp = temp.sibling;
								}
								if (temp.tag.equals("li"))
									temp.tag = "p";
								temp.sibling = curr.sibling;
								curr.firstChild = null;
								curr.sibling = null;
							}
							else {
								curr.firstChild.sibling = curr.sibling;
								prev.sibling = curr.firstChild;
							}
						}
						// If the target does not have a sibling
						else {
							if (curr.firstChild.sibling != null) {
								TagNode temp = curr.firstChild;
								while(temp.sibling != null) {
									if (temp.tag.equals("li"))
										temp.tag = "p";
									temp = temp.sibling;
								}
								if (temp.tag.equals("li"))
									temp.tag = "p";
								prev.sibling = curr.firstChild;
							}
							else {
								if (curr.firstChild.tag.equals("li"))
									curr.firstChild.tag = "p";
								prev.sibling = curr.firstChild;
							}
						}
					}
				}
				}
		
		// Inorder Traversal
		removeTag2(curr, curr.firstChild, tag2);
		removeTag2(curr, curr.sibling, tag2);
	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word
	 *            Word around which tag is to be added
	 * @param tag
	 *            Tag to be added
	 */
	public void addTag(String word, String tag) {
		
		addTag2(null, root, word, tag);
	}
	
	private void addTag2(TagNode prev, TagNode curr, String word2, String tag2) {
		
		// Base Case
		if (curr == null)
			return;
		
		// Prevents nesting of two of the same tags
		if(prev != null && prev.tag.equals(tag2)){
			return;
		}
		
		if (tag2.equals("html") || tag2.equals("body") || tag2.equals("p") || tag2.equals("em") || tag2.equals("b") ||
				tag2.equals("table") || tag2.equals("tr") || tag2.equals("td") || tag2.equals("ol") || tag2.equals("ul") || tag2.equals("li")) {
		
		if(curr.tag.equals("html") || curr.tag.equals("body") || curr.tag.equals("p") || curr.tag.equals("em") || curr.tag.equals("b") ||
			curr.tag.equals("table") || curr.tag.equals("tr") || curr.tag.equals("td") || curr.tag.equals("ol") || curr.tag.equals("ul") || curr.tag.equals("li")) {
		}
		// Investigate, if curr is plain text, see if there is a match.
		else {
		
			
			String[] array = curr.tag.split(" ");
			int len = array.length;
			String before = "";
			String target = "";
			String after = "";
			TagNode temp = new TagNode(tag2, null, null);
			// Check if there is only 1 word
			if (len == 1) {
				for (int i = 0; i < len; i++) {
					if (specCheck(array[i], word2)) {
						if (prev.firstChild == curr) {
							if (curr.sibling != null) {
								//System.out.println("hey1");
								prev.firstChild = temp;
								temp.firstChild = curr;
								temp.sibling = curr.sibling;
								curr.sibling = null;
							}
							else {
								//System.out.println("hey2");
								prev.firstChild = temp;
								temp.firstChild = curr;
							}
						}
						if (prev.sibling == curr) {
							if (curr.sibling != null) {
								//System.out.println("hey3");
								prev.sibling = temp;
								temp.firstChild = curr;
								temp.sibling = curr.sibling;
								curr.sibling = null;
							}
							else {
								//System.out.println("hey4");
								prev.sibling = temp;
								temp.firstChild = curr;
							}
						}
					}
				}	
			}
				else {
					TagNode head = null;
					TagNode tail = null;
					boolean beforeCheck = true;
					boolean targetCheck = true;
					boolean afterCheck = true;
					while (afterCheck == true) {
						TagNode beforeTN = new TagNode(null, null, null);
						TagNode targetTN = new TagNode(null, null, null);
						TagNode afterTN = new TagNode(null, null, null);
						before = "";
						target = "";
						after = "";
						for (int n = 0; n < len && (targetCheck == true); n++) {
							if (specCheck(array[n], word2)) {
								beforeCheck = false;
								targetCheck = false;
								//System.out.println("target, array[n]: " + array[n]);
								target = array[n];
								targetTN.tag = target;
								// Store the rest of the string into an after node
								if (n != len - 1) {
								for (int m = n + 1; m < len; m++) {
									//System.out.println("length: " + len);
									//System.out.println("m: " + m);
									after = after + array[m] + " ";
									//System.out.println("after: " + after);
								}
								afterTN.tag = after;
								}
							}
							else if (beforeCheck == true) {
								before = before + array[n] + " ";
								beforeTN.tag = before;
								//System.out.println("before: " + before);
							}
						}
						
						// If there was never a match
						if (targetCheck == true){
							if (prev.firstChild == curr)
								prev.firstChild = beforeTN;
							if (prev.sibling == curr)
								prev.sibling = beforeTN;
							break;
						}
						// Set the pointers. Chain the nodes
						if (beforeTN.tag != null && targetTN.tag != null && afterTN.tag != null) {
							beforeTN.sibling = temp;
							temp.firstChild = targetTN;
							temp.sibling = afterTN;
						}
						else if (beforeTN.tag != null && targetTN.tag != null) {
							beforeTN.sibling = temp;
							temp.firstChild = targetTN;
							
						}
						else if (afterTN.tag != null) {
							temp.firstChild = targetTN;
							temp.sibling = afterTN;
						}
						
						if (head == null && beforeTN.tag != null)
							head = beforeTN;
						else if (head == null && beforeTN.tag == null) {
							temp.firstChild = targetTN;
							head = temp;
						}
						else
							tail = targetTN;
						
						if (afterTN.tag != null) {
							afterCheck = true;
							array = afterTN.tag.split(" ");
							len = array.length;
							if (head == null && beforeTN.tag != null)
								head = beforeTN;
							else if (head == null && beforeTN.tag == null) {
								temp.firstChild = targetTN;
								head = temp;
							}
							else
								tail = afterTN;
						}
						else
							afterCheck = false;
					}
					
					if (prev.firstChild == curr)
						prev.firstChild = head;
					else if (prev.sibling == curr)
						prev.sibling = head;
					if(curr.sibling == null) {
						
					}
					else {
						tail.sibling = curr.sibling;
						curr.sibling = null;
					}
				}

				//System.out.println(Arrays.toString(array));
		}
		
		
		addTag2(curr, curr.firstChild, word2, tag2);
		addTag2(curr, curr.sibling,  word2, tag2);
		}
	}
	// A special check to replace .equals
	private boolean specCheck (String currword, String targetword) {
		String currword2 = currword.toLowerCase();
		String targetword2 = targetword.toLowerCase();
		
		// See if they simply equal each other
		if(currword2.equals(targetword2))
			return true;
		
		// Check if the final character is a punctuation
		char last = currword.charAt(currword.length() - 1);
		
		// Check if the last character is a letter (ie to prevent addTag to tagging cows while searching word: cow)
		if (Character.isLetter(last))
			return false;
		
		// Remove the last character of the current target word, if the words match, return true. if not, return false
		else if (targetword2.equals(currword2.substring(0, currword.length() - 1)))
			return true;
		else
			return false;
	}
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the input
	 * file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr = root; ptr != null; ptr = ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");
			}
		}
	}

}
