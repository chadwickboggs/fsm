# Finite State Machine (FSM)

Each FSM instances contains a name, a data item, and a state diagram.  It may recieven events.  If they events are unrecognized then 
either an exception gets thrown or no action gets taken.  If, however, the event is recognized, then any corresponding customer handler 
code may get executed then the current state possibly changed.  Cyclic state transition diagrams are supported through use of the 
Transition.toStateName field naming the cyclic static.  All other transition should use the Transition.toState field.

Each State may include a data item.  Each event may include a data argument.  JSON Serialization/Deserialization is support for the 
state diagram/configuration, but not for the custom event handler code blocks.
