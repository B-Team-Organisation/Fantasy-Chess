# Tutorial

Ensure you have node version 18 or higher installed.

**Start in watch mode:**
```sh
    npx @marp-team/marp-cli@latest -w presentation.md -o local.presentation.html
```

Once you ran this command, a html file with the contents of the presentation, as we will use it is rendered
into the `out/` directory as `presentation.html` simply open this HTML file using the idea browser preview. It will
automatically update and display changes as you make them.

**Convert to pdf:**
```sh
  npx @marp
  -team/marp-cli@latest presentation.md -o out/fantasy-chess-presentation.pdf
```
