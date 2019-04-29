import { Track } from './track';

export class Playlist{
    playlistId: number;
    title: string;
    imageUrl: string;
    username: string;
    duration: number;
    genres: string[];
    tracks: Track[];
}