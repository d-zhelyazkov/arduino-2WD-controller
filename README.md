# arduino-2WD-controller
2WD Software Controller for Arduino with MotorShield

Features:
- wheel encoders for constant wheel motions
- API for movement(forward/backward) and turning(left/right), measured with wheel rotations, units and turn degrees
- communication over the default USB serial link
- precise motions based on physical parameters like wheel diameter and axle track

Architecture:
- reactive design, based on serial events (USB) and low-level interrupts (wheel encoders)

Depends on:
- motor library
- interrupt_system library
- stream library
- tools
    * these libraries are in other repository of mine, called "Arduino"