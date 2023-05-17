package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.tile.Material;

public class PlanetTileSet {
    
    private Material sea = Material.LAVA; 
    private Material dirt = Material.DIRT;
    private Material crust = Material.SEDIMENTARY_ROCK;
    private Material mantle = Material.IGNEOUS_ROCK;
    private Material metal = Material.COPPER_ORE;
    private Material caveAir = Material.EMPTY;

    public PlanetTileSet() {

    }

    public PlanetTileSet(PlanetTileSet set) {
        this.sea = set.sea;
        this.dirt = set.dirt;
        this.crust = set.crust;
        this.mantle = set.mantle;
        this.metal = set.metal;
        this.caveAir = set.caveAir;
    }

    public PlanetTileSet ocean() {
        Material sea = this.sea;
        this.sea = this.crust;
        this.caveAir = this.metal;

        this.crust = sea;
        this.mantle = sea;
        this.metal = sea;
        this.dirt = Material.EMPTY;
        return this;
    }

    public Material sea() {
        return sea;
    }

    public Material dirt() {
        return dirt;
    }

    public Material crust() {
        return crust;
    }

    public Material mantle() {
        return mantle;
    }

    public Material metal() {
        return metal;
    }

    public Material caveAir() {
        return caveAir;
    }

    public PlanetTileSet sea(Material sea) {
        this.sea = sea;
        return this;
    }

    public PlanetTileSet dirt(Material dirt) {
        this.dirt = dirt;
        return this;
    }

    public PlanetTileSet crust(Material crust) {
        this.crust = crust;
        return this;
    }

    public PlanetTileSet mantle(Material mantle) {
        this.mantle = mantle;
        return this;
    }

    public PlanetTileSet metal(Material metal) {
        this.metal = metal;
        return this;
    }

    public PlanetTileSet caveAir(Material caveAir) {
        this.caveAir = caveAir;
        return this;
    }

}
