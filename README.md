# Vinyls

A Fabric mod that allows making server-side custom music discs.

## Quick Start

### 1. Add Sound Files

Place your `.ogg` sound files in:

```
config/vinyls/sounds/
```

The filename becomes the sound ID (e.g., `custom_music.ogg` → `vinyls:custom_music`)

### 2. Add Behavior to Your Filament Item

Add the `vinyls:music_disc` behavior to any [Filament item definition](https://tomalbrc.de/docs/filament/content/item/items.html):

```json
"behaviour": {
  "vinyls:music_disc": {
    "sound_event": "vinyls:custom_music",
    "description": "Furq - Custom Music",
    "length_in_seconds": 112.0,
    "comparator_output": 15,
    "range": 64
  }
}
```

### 3. Restart Server

That's it! Your custom music disc is ready to use.
