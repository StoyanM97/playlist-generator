export class PlaylistGenerator{
    title: string;
    travelFrom: string;
    travelTo: string;
    genres: Genre[];
    allowSameArtists: boolean;
    useTopTracks: boolean;
    username: string;
}

export class Genre{
    genre: string;
    percentage: number;
}