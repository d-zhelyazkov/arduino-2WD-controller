# arduino-2WD-controller
2WD Software Controller for Arduino with MotorShield

Features:
- wheel encoders for precise wheel movements
- API for movement(forward/backward) and turning(left/right), evaluated with wheel rotations
- communication over the default USB serial link

Architecture:
- reactive design, based on serial events (USB) and low-level interrupts (wheel encoders)
- state machine for the communication

Depends on:
- motor library
- interrupt_system library
- stream library
    * these libraries are in other repository of mine, called "Arduino"