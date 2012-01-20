# This compiles the natives on my x86_64 machine (paths might not match on yours).
g++ -o ../bin/libLinuxRemote_x86_64.so -shared -Wl,-soname,libLinuxRemote_x86_64.so -I/opt/java/include -I/opt/java/include/linux -lX11 -lXtst remote.cpp -lc -fPIC
