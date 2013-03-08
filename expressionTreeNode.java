import java.lang.Math.*;

class expressionTreeNode {
    private Object value;
    private expressionTreeNode leftChild, rightChild, parent;
    
    expressionTreeNode() {
        value = null; 
        leftChild = rightChild = parent = null;
    }
    
    // Constructor
    /* Arguments: String s: Value to be stored in the node
                  expressionTreeNode l, r, p: the left child, right child, and parent of the node to created      
       Returns: the newly created expressionTreeNode               
    */
    expressionTreeNode(String s, expressionTreeNode l, expressionTreeNode r, expressionTreeNode p) {
        value = s; 
        leftChild = l; 
        rightChild = r;
        parent = p;
    }
    
    /* Basic access methods */
    Object getValue() { return value; }

    expressionTreeNode getLeftChild() { return leftChild; }

    expressionTreeNode getRightChild() { return rightChild; }

    expressionTreeNode getParent() { return parent; }


    /* Basic setting methods */ 
    void setValue(Object o) { value = o; }
    
    // sets the left child of this node to n
    void setLeftChild(expressionTreeNode n) { 
        leftChild = n; 
        n.parent = this; 
    }
    
    // sets the right child of this node to n
    void setRightChild(expressionTreeNode n) { 
        rightChild = n; 
        n.parent=this; 
    }
    

    // Returns the root of the tree describing the expression s
    // Watch out: it makes no validity checks whatsoever!
    expressionTreeNode(String s) {
        // check if s contains parentheses. If it doesn't, then it's a leaf
        if (s.indexOf("(")==-1) setValue(s);
        else {  // it's not a leaf

            /* break the string into three parts: the operator, the left operand,
               and the right operand. ***/
            setValue( s.substring( 0 , s.indexOf( "(" ) ) );
            // delimit the left operand 2008
            int left = s.indexOf("(")+1;
            int i = left;
            int parCount = 0;
            // find the comma separating the two operands
            while (parCount>=0 && !(s.charAt(i)==',' && parCount==0)) {
                if ( s.charAt(i) == '(' ) parCount++;
                if ( s.charAt(i) == ')' ) parCount--;
                i++;
            }
            int mid=i;
            if (parCount<0) mid--;

        // recursively build the left subtree
            setLeftChild(new expressionTreeNode(s.substring(left,mid)));
    
            if (parCount==0) {
                // it is a binary operator
                // find the end of the second operand.07
                while ( ! (s.charAt(i) == ')' && parCount == 0 ) )  {
                    if ( s.charAt(i) == '(' ) parCount++;
                    if ( s.charAt(i) == ')' ) parCount--;
                    i++;
                }
                int right=i;
                setRightChild( new expressionTreeNode( s.substring( mid + 1, right)));
        }
    }
    }


    // Returns a copy of the subtree rooted at this node... 2013
    expressionTreeNode deepCopy() {
        expressionTreeNode n = new expressionTreeNode();
        n.setValue( getValue() );
        if ( getLeftChild()!=null ) n.setLeftChild( getLeftChild().deepCopy() );
        if ( getRightChild()!=null ) n.setRightChild( getRightChild().deepCopy() );
        return n;
    }
    
    // Returns a String describing the subtree rooted at a certain node.
    public String toString() {
        String ret = (String) value;
        if ( getLeftChild() == null ) return ret;
        else ret = ret + "(" + getLeftChild().toString();
        if ( getRightChild() == null ) return ret + ")";
        else ret = ret + "," + getRightChild().toString();
        ret = ret + ")";
        return ret;
    } 


    // Returns the value of the the expression rooted at a given node
    // when x has a certain value
    double evaluate(double x) {
        System.out.println(this.getLeftChild() + " " + this.getRightChild());
    	if (this.getValue().equals("mult")) {
    		//System.out.println("Mult: " + this.getLeftChild().evaluate(x) * this.getRightChild().evaluate(x));
    		return (this.getLeftChild().evaluate(x) * this.getRightChild().evaluate(x));
    	} else if (this.getValue().equals("add")) {
    		System.out.println("Add: " + this.getLeftChild().evaluate(x) + this.getRightChild().evaluate(x));
    		return (this.getLeftChild().evaluate(x) + this.getRightChild().evaluate(x));
    	} else if (this.getValue().equals("minus")) {
    		return (this.getLeftChild().evaluate(x) - this.getRightChild().evaluate(x));
    	} else if (this.getValue().equals("sin")) {
    		return (Math.sin(this.getLeftChild().evaluate(x)));
    	} else if (this.getValue().equals("cos")) {
    		//System.out.println("Cos: " + Math.cos(this.getLeftChild().evaluate(x)));
    		return (Math.cos(this.getLeftChild().evaluate(x)));
    	} else if (this.getValue().equals("exp")) {
    		return (Math.exp(this.getLeftChild().evaluate(x)));
    	} else if (this.getValue().equals("x")) {
    		System.out.println("x Leaf");
    		return x;
    	} else {
    		System.out.println("Leaf");
    		String value = this.toString();
    		return Integer.parseInt(value);
    	}
    }                         

    /* returns the root of a new expression tree representing the derivative of the
    expression represented by the tree rooted at the node on which it is called ***/
    expressionTreeNode differentiate() {
    	expressionTreeNode derivative = this.deepCopy();
    	if (derivative.getValue().equals("mult")) {
    		//f(x)*g(x): set to (df(x)/dx * g(x)) + (dg(x)/dx * f(x))
    		derivative.setValue("add");
    	} else if (derivative.getValue().equals("add")) {
    		//f(x) + g(x): set to df(x)/dx + dg(x)/dx
    	} else if (derivative.getValue().equals("minus")) {
    		//f(x) - g(x): set to df(x)/dx - dg(x)/dx
    	} else if (derivative.getValue().equals("sin")) {
    		//sin(f(x)): set to cos(f(x)) * df(x)/dx
    	} else if (derivative.getValue().equals("cos")) {
    		//cos(f(x)): set to -sin(f(x)) * df(x)/dx
    		
    		derivative.setValue("mult");
    	} else if (derivative.getValue().equals("exp")) {
    		//exp(f(x)): set to exp(f(x)) * df(x)/dx
    		derivative.setRightChild(derivative.getLeftChild().differentiate());	//Sets the right child to be the derivative of the function in the exponent.
    		derivative.setLeftChild(derivative.deepCopy());							//Sets the left child to be a copy of everything below the exponent in the tree.
    		derivative.setValue("mult");
    	} else if (derivative.getValue().equals("x")) {
    		derivative.setValue("1");												//Base case
    	} else {
    		derivative.setValue("0");												//Base case
    	}
    	return derivative;
    }
    
    /* Extra-credit */
    expressionTreeNode simplify() {
    // WRITE YOUR CODE HERE

    return null; // remove this
    }

    
    public static void main(String args[]) {
        expressionTreeNode e = new expressionTreeNode("mult(add(2,x),cos(x))");
        System.out.println(e);
        System.out.println(e.evaluate(1));
        System.out.println(e.differentiate());
    }
}
