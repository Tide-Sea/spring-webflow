# Overview

Spring Web Flow facilitates building web applications that require guided navigation -- 
e.g. a shopping cart, flight check-in, a loan application, and many others. 
In contrast to stateless, free-form navigation such use cases have a clear start and end
point, one or more screens to go through in a specific order, and a set of changes
that are not finalized to the end.

A distinguishing feature is the ability to define a **flow definition** consisting of
*states*, *transitions*, and *data*. For example, view states correspond to the
individual screens of the flow while transitions are caused by events resulting from
the click of a button or a link. Data may be stored in scopes such as
*flash*, *view*, *flow*, and others. Scoped data is cleared when it is no longer in scope.

In REST terms a flow represents as a single resource. 
The same URL used to start the flow is also the URL used to step through the flow
(there is also an execution key uniquely identifying the current flow instance).
As a result of this approach navigation remains encapsulated in the flow definition.

Some key benefits of using Spring Web Flow:

+ A flow abstraction to model *"long conversations"* in web applications
+ Proper encapsulation for navigation rules
+ Multiple scopes in which to keep data
+ Automatic use of the POST/REDIRECT/GET pattern to avoid browser warnings
+ Impossible to return to completed flow sessions via browser back button
+ Rapid prototyping of flow requirements
+ Development mode in which flow definition changes are detected on the fly
+ IDE visualization for flow definitions
+ Much more...
