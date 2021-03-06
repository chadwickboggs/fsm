package com.tiffanytimbric.fsm;


import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class Main {

    public static void main( @Nonnull final String... args ) throws IOException {
        if ( ArrayUtils.isEmpty( args ) ) {
            exerciseTestFsm();
        }
        else {
            exerciseSpecifiedFsm( args[0] );
        }
    }

    private static void exerciseSpecifiedFsm( @Nonnull final String filenameFsmJson ) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader( Paths.get( filenameFsmJson ) )) {
            final FiniteStateMachine<String> fsm = FiniteStateMachine.fromJson(
                bufferedReader.lines().collect( Collectors.joining( "\n" ) ) );

            System.out.printf( "%nFSM Definition:%n%s%n", fsm.toJson() );

            try ( Scanner scanner = new Scanner( System.in ) ) {
                do {
                    State<String> currentState = fsm.getCurrentState();
                    System.out.printf(
                        "%n\tCurrent State: \"%s\", \"%s\"%n",
                        currentState.name(), currentState.dataItem()
                    );
                    System.out.printf(
                        "\tSupported Events: %s%n",
                        currentState.getEvents().stream().map( Event::name ).collect( Collectors.toList() )
                    );

                    System.out.print( "Send event: " );
                    fsm.handleEvent( scanner.next() );
                }
                while ( true );
            }
        }
    }

    private static void exerciseTestFsm() {
        final FiniteStateMachine<String> fsm = createFsm( "Test FSM" );

        final String fsmJson = fsm.toJson();
        System.out.printf( "FSM JSON Definition:%n%s%n", fsmJson );

        System.out.printf( "%n%s, %s: Start%n", fsm.getName(), fsm.getDataItem() );
        State<String> currentState = fsm.getCurrentState();
        System.out.printf( "\tCurrent State: \"%s\", \"%s\"%n", currentState.name(), currentState.dataItem() );
        System.out.printf(
            "\tSupported Events: %s%n",
            currentState.getEvents().stream().map( Event::name ).collect( Collectors.toList() )
        );

        sendEventVerbose( "Lunch Time", "event_dataArg", fsm );
        sendEventVerbose( "Bus Arrives", "event_dataArg", fsm );
        sendEventVerbose( "Afternoon Shift Start", "event_dataArg", fsm );
        sendEventVerbose( "Postpone Shift Start", "event_dataArg", fsm );
        sendEventVerbose( "Afternoon Shift Start", "event_dataArg", fsm );

        System.out.printf( "%s: End%n", fsm.getName() );

        final FiniteStateMachine<String> fsmFromJson = FiniteStateMachine.fromJson( fsmJson );
        System.out.printf( "%nFSM From-JSON JSON Definition:%n%s%n", fsmFromJson.toJson() );
    }

    private static <T> void sendEventVerbose(
        @Nonnull final String eventName,
        @Nullable final T event_dataArg,
        @Nonnull final FiniteStateMachine<T> fsm ) {
        System.out.printf( "%n\tSending event: \"%s\"%n", eventName );

        fsm.handleEvent( eventName, event_dataArg );
        final State<T> currentState = fsm.getCurrentState();

        System.out.printf( "\tCurrent State: \"%s\", \"%s\"%n", currentState.name(), currentState.dataItem() );
        System.out.printf(
            "\tSupported Events: %s%n",
            currentState.getEvents().stream().map( Event::name ).collect( Collectors.toList() )
        );
    }

    @Nonnull
    private static FiniteStateMachine<String> createFsm( @Nonnull final String fsmName ) {
        final Event<String> afternoonShiftStartEvent =
            new Event( "Afternoon Shift Start", "event_dataArg" );
        final Event<String> postponeShiftStartEvent =
            new Event( "Postpone Shift Start", "event_dataArg" );
        final Event<String> lunchTimeEvent = new Event( "Lunch Time", "event_dataArg" );

        final State<String> endState = new State(
            "Afternoon Shift", "state_dataItem", new Transition<String, String>(
            postponeShiftStartEvent, "At Lunch",
            ( fromState, event ) -> System.out.printf(
                "\thandleEvent(\"%s\", \"%s\"): Transitioning To State \"%s\".%n",
                event.name(), event.dataArg(), "At Lunch" ) ) );

        final State<String> lunchState = new State(
            "At Lunch", "state_dataItem", new Transition<>(
            afternoonShiftStartEvent, endState,
            ( fromState, event ) -> System.out.printf(
                "\thandleEvent(\"%s\", \"%s\"): Transitioning To State \"%s\".%n",
                event.name(), event.dataArg(), endState.name() ) ) );

        final State<String> initialState = new State(
            "Morning Shift", "state_dataItem", new Transition<>(
            lunchTimeEvent, lunchState,
            ( fromState, event ) -> System.out.printf(
                "\thandleEvent(\"%s\", \"%s\"): Transitioning To State \"%s\".%n",
                event.name(), event.dataArg(), lunchState.name() ) ) );

        return new FiniteStateMachine<>( fsmName, "fsm_dataItem", initialState );
    }
}
