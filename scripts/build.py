import subprocess
import platform
import os
import shutil
from pathlib import Path

deferred_prints = []

def deferred_print(msg="", namespace="MAIN"):
    deferred_prints.append(f"[{namespace.upper()}] |>{msg}")

dprint = deferred_print

def run_gradle_build():
    is_windows = platform.system() == "Windows"
    gradle_cmd = "gradlew.bat" if is_windows else "./gradlew"

    if not os.path.exists(gradle_cmd):
        print(f"Error: '{gradle_cmd}' not found in the current directory.")
        return

    try:
        output_buffer = []

        process = subprocess.Popen(
            [gradle_cmd, "build"],
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            universal_newlines=True,
            shell=is_windows
        )

        for line in process.stdout:
            print(line, end="")             # print live
            output_buffer.append(line)      # save for clearing later

        process.wait()
        dprint("Build exited with code " + str(process.returncode), "gradlew")
    except Exception as e:
        dprint(f"Unexpected error: {e}", "gradlew")

def copy_file_to_dir(source_file: Path, target_dir: Path):
    source_file = Path(source_file)
    target_dir = Path(target_dir)
    target_dir.mkdir(parents=True, exist_ok=True)

    if not source_file.is_file():
        raise FileNotFoundError(f"Source file does not exist: {source_file}")

    destination = target_dir / source_file.name
    shutil.copy2(source_file, destination)
    dprint(f"Copied \n\t{source_file} \n\t\tto\n\t{destination}", "copy")

def clear_terminal():
    os.system('cls' if os.name == 'nt' else 'clear')

if __name__ == "__main__":
    run_gradle_build()

    builder_modpack_mods_dir = Path("G:/PrismMC/Instances/1.21.1(1)/minecraft/mods")
    jar_path = Path("G:/mc/FUN/CustomMods/stellarfactory-template-1.21.1/build/libs/stellarfactory-1.0.0.jar")

    copy_file_to_dir(jar_path, builder_modpack_mods_dir)

    # Clear previous build output and show only final messages
    clear_terminal()

    for msg in deferred_prints:
        print(msg)
