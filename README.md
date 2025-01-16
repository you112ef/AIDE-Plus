<p align="center">
  <img src=".idea/icon.svg" style="width: 30%;" />
</p>

> [!TIP]
> This project is built using Android Studio
> It uses Kotlin, so AIDE+ cannot be self-hosted
> If you want self-hosting capability, please visit the main project

- [Chinese README](README_zh.md)
- [Main Project](https://github.com/AndroidIDE-CN/AIDE-Plus)

# AIDE-Plus

## Repository Information
[![GitHub contributors](https://img.shields.io/github/contributors/neu233/AIDE-Plus)](https://github.com/neu233/AIDE-Plus/graphs/contributors)
[![GitHub last commit](https://img.shields.io/github/last-commit/neu233/AIDE-Plus)](https://github.com/neu233/AIDE-Plus/commits/)
[![Repository Size](https://img.shields.io/github/repo-size/neu233/AIDE-Plus)](https://github.com/neu233/AIDE-Plus)
[![GitHub Release](https://img.shields.io/github/v/release/neu233/AIDE-Plus)](https://github.com/neu233/AIDE-Plus/releases)
[![Total downloads](https://img.shields.io/github/downloads/neu233/AIDE-Plus/total)](https://github.com/neu233/AIDE-Plus/releases)

## Build Environment
[![Android Studio](https://img.shields.io/badge/Android_Studio-2024.2.2-red?style=for-the-badge&logo=AndroidStudio)](https://developer.android.com/studio)
[![Gradle](https://img.shields.io/badge/Gradle_Version-8.10.2-red?style=for-the-badge&logo=Gradle)](https://developer.android.com/studio)

## Implemented Features
- [x] Build service optimization and rewrite
- [x] Replaced aapt with aapt2
- [x] Replaced dx with D8
- [x] Implementation of Java 8 syntax features (default syntax analysis)
- [x] Implementation of Java 9 syntax features (default syntax analysis)
- [x] Implementation of Java 11 syntax features (default syntax analysis)
- [x] Java 23 compilation (implemented through ecj, requires enabling in settings)
- [x] High-version Java formatting (implemented through ecj, requires enabling in settings)
- [x] Custom class parser for high-version class features
- [x] Implementation of `runtimeOnly`, `compileOnly`, `libgdxNatives`
- [x] Rewritten Gradle parser
- [x] New Maven downloader (bom support)
- [x] Application cold start optimization
- [x] Added more syntax highlighting
- [x] D8-based obfuscation
- [x] Replaced AIDE default framework with AndroidX
- [x] Restored partial AIDE dex obfuscation
- [x] Custom code coloring
- [x] ViewBinding support
- [x] DataBinding support
- [x] Updated manifest merger tool
- [x] Fixed silent installation and added support for more installers (shizuku)
- [x] New UI implementation
- [x] Fixed completion and highlighting loss issues
- [x] Fixed signature creation issues
- [x] ApkSign support for v1-v4 signatures
- [x] Added basic syntax completion
- [x] Fixed git issues within the application
- [x] APK resource alignment
- [x] Android API support for Java projects
- [x] Lambda implementation (ecj) [fbf450d](https://github.com/AndroidIDE-CN/AIDE-Plus/commit/fbf450dba15ccaf51a7a6dd77db300d50551e98b)
- [x] CMake build support [e702347](https://github.com/AndroidIDE-CN/AIDE-Plus/commit/e702347df0c10b718df5aeb4798402802334e310)

### Planned Features
- [ ] More completions (LSP)
- [ ] Apks/AAB generation (not added)
- [ ] Manifest file editing
- [ ] Vector graphics retrieval

## Related Resources
- [AIDE-Ndk-Install](https://github.com/ZeroAicy/AIDE-Ndk-Install) NDK installer
- [AIDE-Repair](https://github.com/ZeroAicy/AIDE-Repair) Deobfuscation tool

# Additional Information
- QQ Groups
  * [487145957](https://qm.qq.com/q/W0WJq5qne2)
  * [1002980489](https://qm.qq.com/q/W0WJq5qne2)
- [QQ Channel](https://pd.qq.com/s/auq589py2)
- [Website](https://plus.androidide.cn)

# Special Thanks
- [@墨凡尘轩](https://github.com/ZeroAicy) Project Founder
- [@烂泥扶上墙](https://github.com/eirv) Project Collaborator
- [@原罪](https://github.com/neu233) Project Collaborator
- [@0047ol](https://github.com/0047ol) Project Collaborator
- [@hujiayucc](https://github.com/hujiayucc) Project Collaborator
- [@dev-bz](https://github.com/dev-bz) (不知者来此) provided assistance

- [AndroidIDE](https://github.com/AndroidIDEOfficial/AndroidIDE) Referenced some code
- [CodeAssist](https://github.com/tyron12233/CodeAssist) Referenced some code
- [Termux-app](https://github.com/termux/termux-app) Integrated into the software
- [AIDE-Pro](https://github.com/AndroidIDE-CN/) Referenced UI design