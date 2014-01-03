#!/bin/bash
cd ~/Desktop/testoutputs
files=`ls *.csv | cat | sed 's/^/"/' | sed 's/$/" using 1:5 with lines lw 0.5 title columnhead/' | tr "\\n" ", "`
(cat<<__EOF
set terminal wxt
set datafile separator ","
plot $files
pause -1 "press enter to exit"
__EOF
read -r "enter") | gnuplot
