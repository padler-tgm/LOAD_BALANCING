How to run? Issue the following commands:

javac CalculatorBalancer.java
rmic CalculatorBalancer
javac Server.java
javac CalculatorImpl.java
rmic CalculatorImpl
javac Client.java

The Server and Client should be run on the same machine!

rmiregistry &
java Server

In another shell: java Client
