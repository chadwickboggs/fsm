# Finite State Machine (FSM)

Each FSM instances contains a name, a data item, and a state diagram.  It may
recieven events.  If they events are unrecognized then either an exception gets
thrown or no action gets taken.  If, however, the event is recognized, then any
corresponding customer handler code may get executed and the current state
possibly changed.  Cyclic state transition diagrams are supported through use
of the Transition.toStateName field naming the cyclic static.  All other
transition should use the Transition.toState field.

Each State may include a data item.  Each event may include a data argument.
JSON Serialization/Deserialization is supported for the state
diagram/configuration, but not for the custom event handler code blocks.

## Usage Examples
You may want to add the path to this project's "bin" to your shell's PATH
environment variable.

    $ fsm [test | <fsm-definition-json-file-filename>]

## Example 1: Test FSM
This is an example FSM build via the Java API.

    $ fsm test
    FSM JSON Definition:
    {"@class":"com.tiffanytimbric.fsm.FiniteStateMachine","name":"Test FSM","dataItem":"fsm_dataItem","ignoreUnknownEvents":true,"initialState":{"@class":"com.tiffanytimbric.fsm.State","name":"Morning Shift","dataItem":"state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Lunch Time","dataArg":"event_dataArg"},"toState":{"@class":"com.tiffanytimbric.fsm.State","name":"At Lunch","dataItem":"state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Afternoon Shift Start","dataArg":"event_dataArg"},"toState":{"@class":"com.tiffanytimbric.fsm.State","name":"Afternoon Shift","dataItem":"state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Postpone Shift Start","dataArg":"event_dataArg"},"toState":null,"toStateName":"At Lunch"}]},"toStateName":null}]},"toStateName":null}]}}

    Test FSM, fsm_dataItem: Start
        Current State: "Morning Shift", "state_dataItem"
        Supported Events: [Lunch Time]

	    Sending event: "Lunch Time"
	    handleEvent("Lunch Time", "event_dataArg"): Transitioning To State "At Lunch".
	    Current State: "At Lunch", "state_dataItem"
	    Supported Events: [Afternoon Shift Start]

	    Sending event: "Bus Arrives"
	    Current State: "At Lunch", "state_dataItem"
	    Supported Events: [Afternoon Shift Start]

	    Sending event: "Afternoon Shift Start"
	    handleEvent("Afternoon Shift Start", "event_dataArg"): Transitioning To State "Afternoon Shift".
	    Current State: "Afternoon Shift", "state_dataItem"
	    Supported Events: [Postpone Shift Start]

	    Sending event: "Postpone Shift Start"
	    handleEvent("Postpone Shift Start", "event_dataArg"): Transitioning To State "At Lunch".
	    Current State: "At Lunch", "state_dataItem"
	    Supported Events: [Afternoon Shift Start]

	    Sending event: "Afternoon Shift Start"
	    handleEvent("Afternoon Shift Start", "event_dataArg"): Transitioning To State "Afternoon Shift".
	    Current State: "Afternoon Shift", "state_dataItem"
	    Supported Events: [Postpone Shift Start]
    Test FSM: End

    FSM From-JSON JSON Definition:
    {"@class":"com.tiffanytimbric.fsm.FiniteStateMachine","name":"Test FSM","dataItem":"fsm_dataItem","ignoreUnknownEvents":true,"initialState":{"@class":"com.tiffanytimbric.fsm.State","name":"Morning Shift","dataItem":"state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Lunch Time","dataArg":"event_dataArg"},"toState":{"@class":"com.tiffanytimbric.fsm.State","name":"At Lunch","dataItem":"state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Afternoon Shift Start","dataArg":"event_dataArg"},"toState":{"@class":"com.tiffanytimbric.fsm.State","name":"Afternoon Shift","dataItem":"state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Postpone Shift Start","dataArg":"event_dataArg"},"toState":null,"toStateName":"At Lunch"}]},"toStateName":null}]},"toStateName":null}]}}

## Example 2: Entity View Mode FSM
This is a custom FSM read from a JSON file.  It is interacted with dynamically
on the command shell via the "Send Event:" command prompt. 

    $ fsm fsm-entity-view-mode.json

    FSM Definition:
    {"@class":"com.tiffanytimbric.fsm.FiniteStateMachine","name":"Entity View Mode FSM","dataItem":"fsm_dataItem","ignoreUnknownEvents":true,"initialState":{"@class":"com.tiffanytimbric.fsm.State","name":"Readonly","dataItem":"readonly_state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Delete","dataArg":"delete_event_dataArg"},"toState":{"@class":"com.tiffanytimbric.fsm.State","name":"Deleted","dataItem":"deleted_state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Undelete","dataArg":"undelete_event_dataArg"},"toState":null,"toStateName":"Readonly"}]},"toStateName":null},{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Edit","dataArg":"edit_event_dataArg"},"toState":{"@class":"com.tiffanytimbric.fsm.State","name":"In Edit","dataItem":"in_edit_state_dataItem","transitions":[{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Save","dataArg":"save_event_dataArg"},"toState":null,"toStateName":"Readonly"},{"@class":"com.tiffanytimbric.fsm.Transition","event":{"@class":"com.tiffanytimbric.fsm.Event","name":"Cancel","dataArg":"cancel_event_dataArg"},"toState":null,"toStateName":"Readonly"}]},"toStateName":null}]}}

	    Current State: "Readonly", "readonly_state_dataItem"
	    Supported Events: [Delete, Edit]
    Send event: Edit

	    Current State: "In Edit", "in_edit_state_dataItem"
	    Supported Events: [Save, Cancel]
    Send event: asdf

	    Current State: "In Edit", "in_edit_state_dataItem"
	    Supported Events: [Save, Cancel]
    Send event: Save

	    Current State: "Readonly", "readonly_state_dataItem"
	    Supported Events: [Delete, Edit]
    Send event: Delete

	    Current State: "Deleted", "deleted_state_dataItem"
	    Supported Events: [Undelete]
    Send event: Undelete

	    Current State: "Readonly", "readonly_state_dataItem"
	    Supported Events: [Delete, Edit]
