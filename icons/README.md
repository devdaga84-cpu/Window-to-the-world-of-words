# Icons

I will use the existing image you confirmed. This repo contains scripts/iconize.ps1 which will generate app_icon.ico and multiple PNG sizes from a high-resolution PNG.

Place your source high-resolution image (e.g., 2048x2048 PNG) at the repo root named `window_world.png` and run the PowerShell script:

PowerShell (run in project root):

  scripts\iconize.ps1

This will create an `icons/` directory and `app_icon.ico` which the jpackage script will look for.
