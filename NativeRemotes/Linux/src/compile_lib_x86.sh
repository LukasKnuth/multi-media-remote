# This script compiles the library on my x86 machine (paths might not match with other machines).
g++ -o ../bin/libLinuxRemote_x86.so -shared -Wl,-soname,libLinuxRemote_x86.so -I/usr/java/latest/include -I/usr/java/latest/include/linux -lX11 -lXtst remote.cpp -lc -fPIC
