#!/bin/bash
cd ~/Desktop/testoutputs
files=`ls *mean.csv | cat | sed 's/^/"/' | sed 's/$/" using 1:2 with lines lw 3 title columnhead/' | tr "\\n" ", "`
(cat<<__EOF
set terminal wxt
set datafile separator ","
plot $files
pause -1 "press enter to exit"
__EOF
read -r "enter") | gnuplot
