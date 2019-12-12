# -*- coding: utf-8 -*
# python 3.7
# 清log

import os
import os.path
import fnmatch

# 清log
def clearlog():
    try:
        print("clearlog begin")
        #cwddir = os.path.split(os.path.realpath(__file__))[0];
        srcdir = os.path.abspath('../../');
        for filename in iter_find_files(srcdir, "*.log"):
            os.remove(filename)
        print("clearlog end success")
    except Exception as e:
        print("clearlog fail", e)

def iter_find_files(path, fnexp):
	for root, dirs, files, in os.walk(path):
		for filename in fnmatch.filter(files, fnexp):
			yield os.path.join(root, filename)

# execute
clearlog()
input("Press <enter>")