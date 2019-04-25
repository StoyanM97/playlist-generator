import { Track } from './track';

export class Playlist{
    playlistId: number;
    title: string;
    username: string;
    duration: number;
    genres: string[];
    tracks: Track[];
}