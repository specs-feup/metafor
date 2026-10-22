{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
  };

  outputs = inputs@{ flake-parts, ... }: flake-parts.lib.mkFlake { inherit inputs; } {
    perSystem = { pkgs, ... }: {
      devShells.default = pkgs.mkShell {
        nativeBuildInputs = with pkgs; [ jdk21 gradle nodejs_22 python3 ];

        shellHook = ''
          export LLVM_HOME="$(realpath ../llvm/install)"
          export PATH="$LLVM_HOME/bin:$PATH"
        '';
      };
    };

    systems = [ "x86_64-linux" "aarch64-linux" "aarch64-darwin" ];
  };
}