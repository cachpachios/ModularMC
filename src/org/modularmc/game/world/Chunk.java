package org.modularmc.game.world;

import java.util.Arrays;

import org.modularmc.game.material.BlockType;

/**
 * @author Caspar Nor�e Palm
 */
public class Chunk {
	private final int x, z;
	
	private final World world;
	
	private ChunkSection[] sections;
	
	private byte[] biomes;
	
	public Chunk(final int x, final int z, World world) {
		this.x = x;
		this.z = z;
		this.world = world;
		
		biomes = new byte[256];
		
		Arrays.fill(biomes, (byte) 1);
		
		sections = new ChunkSection[16];
		
		sections[0] = new ChunkSection(this, 0);
	} 
	
	public final ChunkSection getSection(int y) {
		return sections[y >> 4];
	}
	
	public void tick() {
		
	}
	
	public int countSections() {
		int v = 0;
		for(final ChunkSection s : sections)
			if(s != null)
				++v;
		return v;
	}
	
	public short getSectionBitMap() {
		short r = 0;
		for (final ChunkSection s : sections)
			if (s != null)
				r |= 1 << s.getY();
		return r;
	}
	
	public byte[] getData(boolean skyLight, boolean biomes) {
		int dataSize = 0;
		
		dataSize += 8192 * countSections(); // A short for every block in every section (16*16*16 * 2) * amount of sections
		
		dataSize += 2048 * countSections(); // 0.5 byte for each block (NibbleArray)
		if(skyLight)
			dataSize += 2048 * countSections(); // 0.5 byte for each block (NibbleArray)
		
		if(biomes)
			dataSize += 256;
		
		byte[] data = new byte[dataSize];
		int i = 0;
		
		
        for (final ChunkSection s : sections)
            for (final short b : s.blockTypes()) {
            	data[i++] = (byte) (b & 0xff);
            	data[i++] = (byte) (b >> 8);
            }
        
        for (final ChunkSection s : sections) {
            System.arraycopy(s.blockLightning(), 0, data, i, s.blockLightning().length);
            i += s.blockLightning().length;
            
        }
		
        if(skyLight)
            for (final ChunkSection s : sections) {
                System.arraycopy(s.skyLightning(), 0, data, i, s.skyLightning().length);
                i += s.skyLightning().length;
            }
        
        
		if(biomes)
            for (int j = 0; j < 256; ++j)
                data[i++] = this.biomes[j];
				
		return data;
	}
	
	public void setBlock(int x, int y, int z, BlockType type) {
		int section = y >> 4;
        if(sections[section] != null)
        	sections[section].setBlockType(x, y, z, type.getCodedID());
        else {
        	if(type==BlockType.AIR)
        		return;
        	sections[section] = new ChunkSection(this, section);
        	sections[section].setBlockType(x, y, z, type.getCodedID());
        }
	}

	public void setBiome(int x, int y, Biome biome) {
		biomes[y*16+x] = biome.getID();
	}
	
	public void getBiome(int x, int y, Biome biome) {
		biomes[y*16+x] = biome.getID();
	}
	
	public void fillBiome(Biome biome) {
		Arrays.fill(biomes, biome.getID());
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
}
