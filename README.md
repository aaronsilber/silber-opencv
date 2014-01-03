Hey there!

Welcome to silber-opencv, the repository for JavaGaits. JavaGaits is a hacked-together implementation of OpenCV blob tracking with official Java bindings. It is designed for scientific testing - particularly a research project I have been working on.

The code is freely available in the public domain, as made clear in JavaGaits/UNLICENSE. If it is useful to you, wonderful! I prefer but do not require attribution.

Features?
* Written in Java
* Uses OpenCV 2.4.6
* Supports Video4Linux capture devices
* Tracks a (usually blue) blob (usually a circle) in real-time
* Records this data to CSV for processing with anything else
* Experimental and unpolished

Depends:
* apache commons-io 2.4
* JavaPlot gnuplot interface (not currently used)
* opencv 2.4.6 compiled with Java bindings + V4L2
