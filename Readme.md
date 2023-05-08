# Multi Media Remote

This is an old project I recently rediscovered.

The idea was to build something like the remote on my old CD player. It would allow only previous/next, _but_ it would support this for _any_ player running on my PC.

I implemented this using an Android client application which connected to a server on the computer. The server would, upon receiving the signal, emulate the press of a media-key. This would then be picked up by the applications (such as VLC or Spotify) to switch songs.

I discovered later that Java was not able to dispatch these special key actions to the OS, so I built the dispatch in C and included it as native modules.

This was developed and tested under Java 1.6
