export class Playlist{
    playlistId: number;
    title: string;
    travelFrom: string;
    travelTo: string;
    genres: Map<string,number>;
    allowSameArtists: boolean;
    useTopTracks: boolean;
    username: string;
}