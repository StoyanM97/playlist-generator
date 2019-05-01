import { Track } from './track';

export class Playlist{
    playlistId: number;
    title: string;
    username: string;
    duration: number;
    averageRank: number;
    imageUrl: string;
    genres: string[];
    tracks: Track[];
}