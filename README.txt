Version 1.1

WIP


Overall Goal: Maintain game process, increase efficiency


Extending support for vector position and vector aided animation and value interpolation

Writing customized Swing components to shed the extrafluous properties and inefficient redrawing
(I only need JLabels for thier picture showing capabilities for the most part, so writing a specific "ImageLabel" is better)

Implementing normalized full frame redraws at certain intervals (x FPS)

Add efficiency to animations by implementing Swing Timers instead of Thread.sleep